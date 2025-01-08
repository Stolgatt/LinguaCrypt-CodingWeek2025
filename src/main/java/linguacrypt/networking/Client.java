package linguacrypt.networking;

import linguacrypt.ApplicationContext;

import java.io.*;
import java.net.Socket;
import javafx.application.Platform;

public class Client {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private User user; // Updated: Store the `User` instance
    private ApplicationContext context = ApplicationContext.getInstance();

    public Client(String serverAddress, int port, String nickname, int teamId) throws IOException {
        this.socket = new Socket(serverAddress, port);

        // Create the user and store it
        this.user = new User(nickname, socket.getInetAddress(), teamId);

        // Initialize streams
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());

        // Send a CONNECT message to the server
        Message connectMessage = new Message(MessageType.CONNECT, user.getNickname(), "Joining the game");
        connectMessage.setTeam(user.getTeamId()); // Include the team ID
        sendMessage(connectMessage);

        // Start listening for incoming messages
        listenForMessages();
    }

    private void listenForMessages() {
        new Thread(() -> {
            try {
                while (true) {
                    Message message = (Message) input.readObject();
                    handleMessage(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Disconnected from server: " + e.getMessage());
                closeConnection();
            }
        }).start();
    }

    private void handleMessage(Message message) {
        switch (message.getType()) {
            case CHAT:
                System.out.println(message.getNickname() + ": " + message.getContent());
                Platform.runLater(() -> {
                    context.getLobbyView().addChatMessage(message.getNickname(), message.getContent());
                });
                break;
            case CONNECT_OK:
                System.out.println("Connected to the server!");
                break;
            case CONNECT_FAILED:
                System.out.println("Connection failed: " + message.getContent());
                break;
            case USER_JOINED:
                System.out.println(message.getNickname() + " joined the game.");
                Platform.runLater(() -> {
                    context.getLobbyView().addPlayer(message.getNickname());
                });
                break;
            case USER_LEFT:
                System.out.println(message.getNickname() + " left the game.");
                Platform.runLater(() -> {
                    context.getLobbyView().removePlayer(message.getNickname());
                });
                break;
            case PLAYER_LIST:
                
                Platform.runLater(() -> {
                    ApplicationContext.getInstance().getLobbyView().refreshUserList();
                    ApplicationContext.getInstance().getLobbyView().handlePlayerListUpdate(message.getContent());
                });
                break;
            default:
                System.out.println("Unhandled message type: " + message.getType());
        }
    }

    public void sendMessage(Message message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    public void sendChatMessage(String content) {
        sendMessage(new Message(MessageType.CHAT, user.getNickname(), content));
    }

    public void disconnect() {
        try {
            sendMessage(new Message(MessageType.DISCONNECT, user.getNickname(), ""));
            socket.close();
        } catch (IOException e) {
            System.out.println("Error disconnecting: " + e.getMessage());
        }
    }

    public User getUser() {
        return user;
    }

    private void closeConnection() {
        try {
            if (socket != null)
                socket.close();
            if (input != null)
                input.close();
            if (output != null)
                output.close();
        } catch (IOException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
