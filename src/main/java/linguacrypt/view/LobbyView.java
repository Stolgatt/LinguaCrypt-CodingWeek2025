package linguacrypt.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.Player;
import linguacrypt.networking.Client;
import linguacrypt.networking.Message;
import linguacrypt.networking.MessageType;
import linguacrypt.networking.Server;

public class LobbyView {

    @FXML
    private Label labelServerIP;

    @FXML
    private ListView<String> listViewBlueTeam;

    @FXML
    private ListView<String> listViewRedTeam;

@FXML
private ListView<HBox> listViewChat;

    @FXML
    private TextField textFieldChatMessage;

    @FXML
    private Button buttonStartGame;

    private ApplicationContext context = ApplicationContext.getInstance();
    private ObservableList<String> blueTeamList = FXCollections.observableArrayList();
    private ObservableList<String> redTeamList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Bind the ListViews to the team lists
        listViewBlueTeam.setItems(blueTeamList);
        listViewRedTeam.setItems(redTeamList);

        // Refresh the user lists based on the current game instance
        refreshUserList();

        // Determine whether the client or server is active
        Server server = context.getServer();
        Client client = context.getClient();

        if (server != null) {
            labelServerIP.setText("Server running on port: " + Server.PORT);
            buttonStartGame.setVisible(true); // Only the host can start the game
        } else if (client != null) {
            labelServerIP.setText("Connected to server at: " + client.getSocket().getInetAddress().getHostAddress());
            buttonStartGame.setVisible(false); // Hide start game button for clients
        }
    }

    public void refreshUserList() {
        blueTeamList.clear();
        redTeamList.clear();
        Game game = context.getGame();
        if (game != null) {
            for (Player player : game.getBlueTeam().getPlayers()) {
                blueTeamList.add(player.getName());
            }
            for (Player player : game.getRedTeam().getPlayers()) {
                redTeamList.add(player.getName());
            }
        }
    }

    @FXML
private void sendMessage() {
    String message = textFieldChatMessage.getText().trim();

    if (!message.isEmpty()) {
        textFieldChatMessage.clear();

        Message msg = new Message(
            MessageType.CHAT,
            context.getClient() != null
                ? context.getClient().getUser().getNickname()
                : context.getServer().getHostNickname(),
            message
        );

        int teamId = context.getClient() != null
            ? context.getClient().getUser().getTeamId()
            : 0; // Default to blue team for the host

        // Add the message to the local chat view
        addChatMessage(msg.getNickname(), msg.getContent(), teamId);

        // Broadcast or send the message
        if (context.getServer() != null) {
            context.getServer().broadcastMessage(msg);
        } else if (context.getClient() != null) {
            context.getClient().sendMessage(msg);
        }
    }
}

    @FXML
    private void startGame() {
        if (context.getServer() != null) {
            System.out.println("Starting the game...");
            Message message = new Message(MessageType.GAME_START, "Host", "Starting the game...");

            // Serialize the game instance
            Game game = ApplicationContext.getInstance().getGame();
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(game);
                oos.flush();

                // Add the serialized game to the message
                message.setSerializedGame(bos.toByteArray());
            } catch (IOException e) {
                System.out.println("Error serializing game: " + e.getMessage());
                return;
            }

            // Broadcast the GAME_START message to all clients
            context.getServer().broadcastMessage(message);
            // Switch to game view
            context.getRoot().setCenter(context.getGameNode());
        }
    }

    @FXML
    private void goBackToMainMenu() {
        System.out.println("Returning to main menu...");
        if (context.getServer() != null) {
            context.getServer().stop();
        } else if (context.getClient() != null) {
            context.getClient().disconnect();
        }
        context.getRoot().setCenter(context.getMainMenuNode());
    }

    public void addPlayer(String playerName, int team) {
        if (team == 0) {
            blueTeamList.add(playerName);
        } else if (team == 1) {
            redTeamList.add(playerName);
        }
        addChatMessage(playerName, " joined the " + (team == 0 ? "blue" : "red") + " team.\n", team);
    }

    public void removePlayer(String playerName, int team) {
        if (team == 0) {
            blueTeamList.remove(playerName);
        } else if (team == 1) {
            redTeamList.remove(playerName);
        }
        addChatMessage(playerName, " left the " + (team == 0 ? "blue" : "red") + " team.\n", team);
    }

    public void handlePlayerListUpdate(String playerList) {
        blueTeamList.clear();
        redTeamList.clear();
        for (String player : playerList.split(";")) {
            if (!player.isEmpty()) {
                if (player.startsWith("[Blue]")) {
                    blueTeamList.add(player.substring(6));
                } else if (player.startsWith("[Red]")) {
                    redTeamList.add(player.substring(5));
                }
            }
        }
    }

    public void addChatMessage(String sender, String message, int teamId) {
    // Create a styled HBox for each message
    HBox chatBox = new HBox();
    chatBox.setSpacing(10);
    chatBox.setPadding(new Insets(5));
    
    // Style based on the sender's team
    String backgroundColor = (teamId == 0) ? "#ADD8E6" : "#F08080"; // Blue or Red
    chatBox.setStyle("-fx-background-color: " + backgroundColor + "; -fx-border-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");

    // Create sender label
    Label senderLabel = new Label(sender + ": ");
    senderLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");

    // Create message label
    Label messageLabel = new Label(message);
    messageLabel.setStyle("-fx-text-fill: white;");

    // Add labels to HBox
    chatBox.getChildren().addAll(senderLabel, messageLabel);

    // Add the chatBox to the ListView
    listViewChat.getItems().add(chatBox);

    // Scroll to the bottom for new messages
    listViewChat.scrollTo(chatBox);
}

    public void joinGame() {
        if (context.getClient() != null) {
            System.out.println("Joining the game...");
            context.getRoot().setCenter(context.getGameNode());
        }
    }
}