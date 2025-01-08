package linguacrypt.networking;

import linguacrypt.networking.User;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String nickname;
    private int teamId;

    public Client(String serverAddress, int port, String nickname, int teamId) throws IOException {
        this.socket = new Socket(serverAddress, port);
        this.nickname = nickname;
        this.teamId = teamId;

        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());

        sendMessage(new Message(MessageType.CONNECT, nickname, String.valueOf(teamId)));

        new Thread(this::listenForMessages).start();
    }

    private void listenForMessages() {
        try {
            while (true) {
                Message message = (Message) input.readObject();
                handleMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Disconnected from server.");
        }
    }

    private void handleMessage(Message message) {
        switch (message.getType()) {
            case CHAT:
                System.out.println(message.getNickname() + ": " + message.getContent());
                break;
            case CONNECT_OK:
                System.out.println("Connected to the server!");
                break;
            case CONNECT_FAILED:
                System.out.println("Connection failed: " + message.getContent());
                break;
            case USER_JOINED:
                System.out.println(message.getNickname() + " joined the game.");
                break;
            case USER_LEFT:
                System.out.println(message.getNickname() + " left the game.");
                break;
            default:
                System.out.println("Unhandled message type: " + message.getType());
        }
    }

    public void sendMessage(Message message) {
        try {
            output.writeObject(message);
        } catch (IOException e) {
            System.out.println("Error sending message.");
        }
    }

    public void sendChatMessage(String content) {
        sendMessage(new Message(MessageType.CHAT, nickname, content));
    }

    public void disconnect() {
        try {
            sendMessage(new Message(MessageType.DISCONNECT, nickname, ""));
            socket.close();
        } catch (IOException e) {
            System.out.println("Error disconnecting.");
        }
    }
}
