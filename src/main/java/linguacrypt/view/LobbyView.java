package linguacrypt.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import linguacrypt.ApplicationContext;
import linguacrypt.networking.Message;
import linguacrypt.networking.MessageType;
import linguacrypt.networking.Server;

public class LobbyView {

    @FXML
    private Label labelServerIP;

    @FXML
    private ListView<String> listViewUsers;

    @FXML
    private TextArea textAreaChat;

    @FXML
    private TextField textFieldChatMessage;

    @FXML
    private Button buttonStartGame;

    private ApplicationContext context = ApplicationContext.getInstance();
    private ObservableList<String> userList = FXCollections.observableArrayList();
    private Server server;

    @FXML
    public void initialize() {
        // Bind the user list to the ListView
        listViewUsers.setItems(userList);

        // Retrieve server instance from context
        this.server = context.getServer();

        if (server != null) {
            // Update server IP label
            labelServerIP.setText("Private Server IP Address: " + Server.PORT);
            
            // Add the host to the user list
            userList.add(server.getHostNickname());
        }
    }

    @FXML
    private void sendMessage() {
        String message = textFieldChatMessage.getText().trim();

        if (!message.isEmpty()) {
            textAreaChat.appendText("You: " + message + "\n");
            textFieldChatMessage.clear();

            // Send message to all connected clients
            if (server != null) {
                Message msg = new Message(MessageType.CHAT,"You", message);
                server.broadcastMessage(msg);
            }
        }
    }

    @FXML
    private void startGame() {
        System.out.println("Starting the game...");

        // Transition to game screen
        context.getRoot().setCenter(context.getGameNode());

        // Configure the game settings for multiplayer
        // Additional logic for game initialization can be added here
    }

    @FXML
    private void goBackToMainMenu() {
        System.out.println("Returning to main menu...");
        context.getRoot().setCenter(context.getMainMenuNode());
    }

    public void addPlayer(String playerName) {
        userList.add(playerName);
        textAreaChat.appendText(playerName + " joined the game.\n");
    }
}