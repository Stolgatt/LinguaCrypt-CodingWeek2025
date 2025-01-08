package linguacrypt.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import linguacrypt.ApplicationContext;

public class MultiplayerMenuView {

    @FXML
    private VBox hostView, joinView;

    @FXML
    private TextField textFieldNicknameHost, textFieldNicknameJoin, textFieldAddress;

    @FXML
    private Label labelTeamSize;

    private int teamSize = 4;

    
    private ApplicationContext context = ApplicationContext.getInstance();

    @FXML
    public void initialize() {
        // Initialize the team size label
        labelTeamSize.setText(String.valueOf(teamSize));
    }

    @FXML
    public void showHostView() {
        context.getRoot().setCenter(context.getMPMenuNode());
        hostView.setVisible(true);
        joinView.setVisible(false);
    }

    @FXML
    public void showJoinView() {
        
        context.getRoot().setCenter(context.getMPMenuNode());
        joinView.setVisible(true);
        hostView.setVisible(false);
    }

    @FXML
    private void decreaseTeamSize() {
        if (teamSize > 2) { // Minimum team size
            teamSize--;
            labelTeamSize.setText(String.valueOf(teamSize));
        }
    }

    @FXML
    private void increaseTeamSize() {
        if (teamSize < 10) { // Maximum team size
            teamSize++;
            labelTeamSize.setText(String.valueOf(teamSize));
        }
    }

    @FXML
    private void createRoom() {
        String nickname = textFieldNicknameHost.getText();

        if (nickname.isEmpty() || nickname.length() < 3 || nickname.length() > 15) {
            System.out.println("Nickname must be 3-15 characters long.");
            return;
        }

        System.out.println("Creating room with nickname: " + nickname + " and team size: " + teamSize);
        // Add room creation logic here
    }

    @FXML
    private void joinRoom() {
        String nickname = textFieldNicknameJoin.getText();
        String address = textFieldAddress.getText();

        if (nickname.isEmpty() || nickname.length() < 3 || nickname.length() > 15) {
            System.out.println("Nickname must be 3-15 characters long.");
            return;
        }

        if (address.isEmpty()) {
            System.out.println("Address must not be empty.");
            return;
        }

        System.out.println("Joining room with nickname: " + nickname + " at address: " + address);
        // Add room joining logic here
    }

    @FXML
    private void goBackToMainMenu() {
        // Logic to return to the main menu
        System.out.println("Returning to main menu...");
    }
}
