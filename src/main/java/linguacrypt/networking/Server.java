package linguacrypt.networking;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.Player;

public class Server {
    public static final int PORT = 9001;
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> clients = new ArrayList<>();
    private String hostNickname;
    private ApplicationContext context = ApplicationContext.getInstance();

    public Server(String hostNickname) throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.hostNickname = hostNickname;
    }

    public void start() {
        System.out.println("Server started on port " + PORT);

        // Add the host to the list of clients
        User hostUser = new User(hostNickname, null, 0); // Default team is 0 for the host
        ClientHandler cHandler = new ClientHandler(null);
        clients.add(cHandler);
        cHandler.addUserToTeam(hostUser);
        broadcastPlayerList();

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
                client.stop();
            }
            serverSocket.close();
            System.out.println("Server stopped.");
        } catch (IOException e) {
            System.out.println("Error stopping server: " + e.getMessage());
        }
    }

    public void broadcastMessage(Message message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

public void broadcastPlayerList() {
    Game game = ApplicationContext.getInstance().getGame();
    if (game != null) {
        StringBuilder playerList = new StringBuilder();
        for (Player player : game.getBlueTeam().getPlayers()) {
            playerList.append("[Blue] ").append(player.getName()).append(";");
        }
        for (Player player : game.getRedTeam().getPlayers()) {
            playerList.append("[Red] ").append(player.getName()).append(";");
        }
        Message playerListMessage = new Message(MessageType.PLAYER_LIST, "Server", playerList.toString());
        for (ClientHandler client : clients) {
            client.sendMessage(playerListMessage);
        }
    }
}

    public String getHostNickname() {
        return hostNickname;
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
                // Initialize streams
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());

                // Process the CONNECT message
                Message connectMessage = (Message) input.readObject();
                if (connectMessage.getType() == MessageType.CONNECT) {
                    String nickname = connectMessage.getNickname();
                    int teamId = connectMessage.getTeam(); // Get the team ID from the message

                    // Create and add the user
                    user = new User(nickname, socket.getInetAddress(), teamId);
                    addUserToTeam(user);
                    broadcastPlayerList(); // Notify all clients

                    System.out.println(nickname + " joined team " + teamId);
                    // Notify all connected clients
                    broadcastMessage(new Message(MessageType.USER_JOINED, "Server :", nickname + " joined the game."));
                }

                // Handle messages from the client
                while (!socket.isClosed()) {
                    Message message = (Message) input.readObject();
                    handleMessage(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Client disconnected: " + e.getMessage());
            } finally {
                closeConnection();
            }
        }

        private void addUserToTeam(User user) {
            Game game = ApplicationContext.getInstance().getGame();
            if (game != null) {
                if (user.getTeamId() == 0) {
                    game.getBlueTeam().addPlayer(user.toPlayer());
                } else if (user.getTeamId() == 1) {
                    game.getRedTeam().addPlayer(user.toPlayer());
                }
            }
            context.getLobbyView().refreshUserList();
        }

        public void handleMessage(Message message) {
            if (message.getType() == MessageType.CHAT) {
                broadcastMessage(new Message(MessageType.CHAT, user.getNickname(), message.getContent()));
            }
            // Additional message handling can be added here
            broadcastPlayerList();
        }

        public void sendMessage(Message message) {
            try {
                if (output != null) {
                    output.writeObject(message);
                    output.flush();
                }
            } catch (IOException e) {
                System.out.println("Error sending message to " + user.getNickname() + ": " + e.getMessage());
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
                System.out.println("Client disconnected: " + user.getNickname());
            } catch (IOException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
