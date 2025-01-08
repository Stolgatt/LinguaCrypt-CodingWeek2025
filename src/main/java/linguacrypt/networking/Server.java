package linguacrypt.networking;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
    public static final int PORT = 9001;
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> clients = new ArrayList<>();
    private String hostNickname;

    public Server(String hostNickname) throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.hostNickname = hostNickname;
    }

    public void start() {
        System.out.println("Server started on port " + PORT);

        // Add the host to the list of clients
        User hostUser = new User(hostNickname, null, 0); // Default team is 0 for the host
        clients.add(new ClientHandler(null, hostUser));

        new Thread(() -> {
            try {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getInetAddress());

                    // Create a new ClientHandler for the connected client
                    ClientHandler clientHandler = new ClientHandler(clientSocket, null);
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

    public String getHostNickname() {
        return hostNickname;
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private User user;

        public ClientHandler(Socket socket, User user) {
            this.socket = socket;
            this.user = user;
        }

        @Override
        public void run() {
            try {
                if (socket != null) {
                    // Initialize input and output streams
                    output = new ObjectOutputStream(socket.getOutputStream());
                    input = new ObjectInputStream(socket.getInputStream());

                    // Handle connection
                    Message connectMessage = (Message) input.readObject();
                    if (connectMessage.getType() == MessageType.CONNECT) {
                    InetAddress clientAddress = socket.getInetAddress(); // Use the actual socket's address

                    user = new User(connectMessage.getNickname(), clientAddress, connectMessage.getTeam());
                    System.out.println(user.getNickname() + " joined team " + user.getTeamId());

                    // Notify all users about the new player
                    broadcastMessage(new Message(MessageType.USER_JOINED, "Server", user.getNickname() + " joined the game.", user.getTeamId()));
}
                }

                // Message handling loop
                while (socket != null && !socket.isClosed()) {
                    Message message = (Message) input.readObject();
                    handleMessage(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Client disconnected: " + e.getMessage());
            } finally {
                closeConnection();
            }
        }

        public void handleMessage(Message message) {
            if (message.getType() == MessageType.CHAT) {
                broadcastMessage(new Message(MessageType.CHAT, user.getNickname(), message.getContent()));
            }
            // Additional message handling can be added here
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
