package linguacrypt.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.players.Player;
import linguacrypt.networking.Client;
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

    @FXML
    public void initialize() {
        // Bind the ListView to the user list
        listViewUsers.setItems(userList);

        // Refresh the user list based on the current game instance
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
        userList.clear();
        Game game = context.getGame();
        if (game != null) {
            for (Player player : game.getBlueTeam().getPlayers()) {
                userList.add("[Blue] " + player.getName());
            }
            for (Player player : game.getRedTeam().getPlayers()) {
                userList.add("[Red] " + player.getName());
            }
        }
    }

    @FXML
    private void sendMessage() {
        String message = textFieldChatMessage.getText().trim();

        if (!message.isEmpty()) {
            textAreaChat.appendText("You: " + message + "\n");
            textFieldChatMessage.clear();

            Message msg = new Message(MessageType.CHAT, context.getClient() != null
                    ? context.getClient().getUser().getNickname()
                    : context.getServer().getHostNickname(), message);

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

    public void addPlayer(String playerName) {
        textAreaChat.appendText(playerName + " joined the game.\n");
    }

    public void removePlayer(String playerName) {
        textAreaChat.appendText(playerName + " left the game.\n");
    }

    public void handlePlayerListUpdate(String playerList) {
        userList.clear();
        for (String player : playerList.split(";")) {
            if (!player.isEmpty()) {
                userList.add(player);
            }
        }
    }

    public void addChatMessage(String sender, String message) {
        textAreaChat.appendText(sender + ": " + message + "\n");
    }
}
