package linguacrypt.networking;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javafx.application.Platform;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.players.Player;
import linguacrypt.model.statistique.PlayerStat;

public class Server {
    public static final int PORT = 9001;
    private static ServerSocket serverSocket;
    private ArrayList<ClientHandler> clients = new ArrayList<>();
    private String hostNickname;
    private ApplicationContext context = ApplicationContext.getInstance();
    private User serverUser;

    public Server(String hostNickname) throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.hostNickname = hostNickname;

        // Initialize the host as a player
        this.serverUser = new User(hostNickname, InetAddress.getLocalHost(), 0); // Blue team by default
        addUserToGame(serverUser);
    }

    public void start() {
        System.out.println("Server started on port " + PORT);

        // Notify connected clients about the host joining
        broadcastPlayerList();

        new Thread(() -> {
            try {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getInetAddress());

                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clients.add(clientHandler);
                    new Thread(clientHandler).start();
                }
            } catch (IOException e) {
                System.out.println("Server stopped.");
            }
        }).start();
    }

    public void stop() {
        try {
            for (ClientHandler client : clients) {
                client.closeConnection();
            }
            serverSocket.close();
            System.out.println("Server stopped.");
        } catch (IOException e) {
            System.out.println("Error stopping server: " + e.getMessage());
        }
    }

    public User getServerUser() {
        return serverUser;
    }

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void broadcastPlayerList() {
        Game game = context.getGame();
        if (game != null) {
            StringBuilder playerList = new StringBuilder();
            for (Player player : game.getBlueTeam().getPlayers()) {
                playerList.append("[Blue] ").append(player.getName()).append(";");
            }
            for (Player player : game.getRedTeam().getPlayers()) {
                playerList.append("[Red] ").append(player.getName()).append(";");
            }
            Message playerListMessage = new Message(MessageType.PLAYER_LIST, "Server", playerList.toString());
            broadcastMessage(playerListMessage);
        }
    }

    public void broadcastMessage(Message message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }

        // Add the message locally for the host's view
        if (message.getType() == MessageType.USER_JOINED || message.getType() == MessageType.CHAT) {
            Platform.runLater(() -> context.getLobbyView().addChatMessage(
                    message.getNickname(),
                    message.getContent(),
                    message.getTeam()));
        }
    }

    private void addUserToGame(User user) {
        Game game = context.getGame();
        if (game != null) {
            Player player = game.getPlayerByNickname(user.getNickname());
            if (player == null) {
                // Create a new Player for the user if it doesn't already exist
                player = user.toPlayer(); // Initialize Player in User
                if (user.getTeamId() == 0) {
                    game.getBlueTeam().addPlayer(player);
                } else if (user.getTeamId() == 1) {
                    game.getRedTeam().addPlayer(player);
                }
            } else {
                // If the Player already exists, ensure the User's Player is linked
                if (user.getPlayer() == null) {
                    user.toPlayer();
                }
                user.getPlayer().copyFrom(player);
            }
        }
    
        // Refresh the user list in the LobbyView
        Platform.runLater(() -> context.getLobbyView().refreshUserList());
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private User user;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());

                // Process the CONNECT message
                Message connectMessage = (Message) input.readObject();
                if (connectMessage.getType() == MessageType.CONNECT) {
                    String nickname = connectMessage.getNickname();
                    int teamId = connectMessage.getTeam(); // Get the team ID from the message

                    // Create or retrieve the Player
                    Player player = context.getGame().getPlayerByNickname(nickname);
                    if (player == null) {
                        player = new Player(nickname, false, "", new PlayerStat());
                        if (teamId == 0) {
                            context.getGame().getBlueTeam().addPlayer(player);
                        } else if (teamId == 1) {
                            context.getGame().getRedTeam().addPlayer(player);
                        }
                    }

                    
                    // Create and add the user
                    user = new User(nickname, socket.getInetAddress(), teamId);
                    addUserToGame(user);
                    // Associate the User's Player
                    user.toPlayer().copyFrom(player);
                    
                    // Notify all clients of the updated player list
                    broadcastPlayerList();
                    
                    // Broadcast the join message
                    broadcastMessage(new Message(MessageType.USER_JOINED, nickname, " joined the game.", teamId));
                    
                    
                    context.broadcastGameUpdate();
                    // Refresh the user list in the LobbyView
                    Platform.runLater(() -> context.getLobbyView().refreshUserList());
                }

                while (!socket.isClosed()) {
                    Message message = (Message) input.readObject();
                    if (message.getType() == MessageType.CHAT) {
                        broadcastMessage(message);
                    } else if (message.getType() == MessageType.GAME_UPDATE) {
                        // Deserialize the game object from the message
                        try (ByteArrayInputStream bis = new ByteArrayInputStream(message.getSerializedGame());
                             ObjectInputStream ois = new ObjectInputStream(bis)) {
                            Game updatedGame = (Game) ois.readObject();
                            
                            // Update the server's authoritative game instance
                            context.setGame(updatedGame);
                
                            // Broadcast the updated game to all clients
                            broadcastGameUpdate();
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("Error updating game: " + e.getMessage());
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Client disconnected: " + e.getMessage());
            } finally {
                closeConnection();
            }
        }

        public void broadcastGameUpdate() {
            if (context.getGame() != null) {
                try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                     ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                    oos.writeObject(context.getGame());
                    oos.flush();
        
                    Message gameUpdateMessage = new Message(MessageType.GAME_UPDATE, "Server", "Game state updated");
                    gameUpdateMessage.setSerializedGame(bos.toByteArray());
        
                    broadcastMessage(gameUpdateMessage);
                } catch (IOException e) {
                    System.out.println("Error broadcasting game update: " + e.getMessage());
                }
            }
        }

        public void sendMessage(Message message) {
            try {
                if (output != null) {
                    output.writeObject(message);
                    output.flush();
                }
            } catch (IOException e) {
                System.out.println("Error sending message: " + e.getMessage());
            }
        }

        public void closeConnection() {
            try {
                if (socket != null)
                    socket.close();
                if (input != null)
                    input.close();
                if (output != null)
                    output.close();
                clients.remove(this);

                // Remove the user from the game
                if (user != null) {
                    Game game = context.getGame();
                    if (game != null) {
                        if (user.getTeamId() == 0) {
                            game.getBlueTeam().removePlayer(user.toPlayer());
                        } else if (user.getTeamId() == 1) {
                            game.getRedTeam().removePlayer(user.toPlayer());
                        }
                        broadcastPlayerList();
                    }
                }
            } catch (IOException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public String getHostNickname() {
        return hostNickname;
    }
}
