package linguacrypt.networking;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javafx.application.Platform;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.Player;

public class Server {
    public static final int PORT = 9001;
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> clients = new ArrayList<>();
    private String hostNickname;
    private ApplicationContext context = ApplicationContext.getInstance();
    private User serverUser; // Representing the host as a player

    public Server(String hostNickname) throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.hostNickname = hostNickname;

        // Initialize the host as a player
        this.serverUser = new User(hostNickname, InetAddress.getLocalHost(), 0); // Default team is 0
        addUserToGame(serverUser); // Add the server player to the game
    }

    public void start() {
        System.out.println("Server started on port " + PORT);

        // Notify connected clients about the host joining
        broadcastPlayerList();

        // Handle client connections in a separate thread
        new Thread(() -> {
            try {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getInetAddress());

                    // Create a new ClientHandler for the connected client
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clients.add(clientHandler);

                    // Start the ClientHandler in a new thread
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

    public void sendMessageAsHost(String content) {
        // Simulate the host sending a chat message
        Message message = new Message(MessageType.CHAT, hostNickname, content);
        handleMessage(message, true); // Host messages are flagged as local
    }

    public void broadcastMessage(Message message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
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

    public String getHostNickname() {
        return hostNickname;
    }

    private void addUserToGame(User user) {
        Game game = context.getGame();
        if (game != null) {
            if (user.getTeamId() == 0) {
                game.getBlueTeam().addPlayer(user.toPlayer());
            } else if (user.getTeamId() == 1) {
                game.getRedTeam().addPlayer(user.toPlayer());
            }
        }

        // Refresh the user list in the LobbyView
    Platform.runLater(() -> context.getLobbyView().refreshUserList());
    }

    private void handleMessage(Message message, boolean isLocalHost) {
        switch (message.getType()) {
            case CHAT:
                // Broadcast the chat message to all clients
                System.out.println(message.getNickname() + ": " + message.getContent());

                if (!isLocalHost) {
                    // Rebroadcast the message to all other clients
                    broadcastMessage(message);
                }

                // Add the chat message to the LobbyView's chat area
                Platform.runLater(() -> {
                    context.getLobbyView().addChatMessage(message.getNickname(), message.getContent());
                });
                break;

            case USER_JOINED:
                // Handle a client connecting to the game
                String nickname = message.getNickname();
                int teamId = message.getTeam(); // Get the team ID from the message

                // Add the user to the appropriate team
                User newUser = new User(nickname, null, teamId); // Assuming the InetAddress is not required here
                addUserToTeam(newUser);

                // Notify all clients of the new player
                broadcastPlayerList();
                broadcastMessage(new Message(MessageType.USER_JOINED, "Server", nickname + " joined the game."));

                // Refresh user list on the server
                Platform.runLater(() -> context.getLobbyView().refreshUserList());

                System.out.println(nickname + " connected and joined team " + teamId);
                break;

            case PLAYER_LIST:
                // Update player list (sent by the server to clients)
                System.out.println("Player list updated.");
                broadcastPlayerList();
                break;

            default:
                System.out.println("Unhandled message type: " + message.getType());
                break;
        }
    }

    private void addUserToTeam(User user) {
        Game game = context.getGame();
        if (game != null) {
            if (user.getTeamId() == 0) {
                game.getBlueTeam().addPlayer(user.toPlayer());
            } else if (user.getTeamId() == 1) {
                game.getRedTeam().addPlayer(user.toPlayer());
            }
        }

        // Refresh the LobbyView's user list
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

        public User getUser() {
            return user;
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

                    // Create and add the user
                    user = new User(nickname, socket.getInetAddress(), teamId);
                    addUserToGame(user);
                    broadcastPlayerList(); // Notify all clients

                    System.out.println(nickname + " joined team " + teamId);
                    // Notify all connected clients
                    broadcastMessage(new Message(MessageType.USER_JOINED, nickname, " joined the game."));
                }

                // Handle messages from the client
                while (!socket.isClosed()) {
                    Message message = (Message) input.readObject();
                    handleMessage(message, false); // Client messages are not local
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Client disconnected: " + e.getMessage());
            } finally {
                closeConnection();
            }
        }

        public void sendMessage(Message message) {
            try {
                if (output != null) {
                    output.writeObject(message);
                    output.flush();
                }
            } catch (IOException e) {
                System.out.println("Error sending message to " + (user != null ? user.getNickname() : "Unknown") + ": " + e.getMessage());
            }
        }

        public void closeConnection() {
            try {
                if (socket != null) {
                    socket.close();
                }
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
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
                System.out.println("Client disconnected: " + (user != null ? user.getNickname() : "Unknown"));
            } catch (IOException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
