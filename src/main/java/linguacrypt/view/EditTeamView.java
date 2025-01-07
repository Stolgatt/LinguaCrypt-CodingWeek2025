package linguacrypt.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import linguacrypt.controller.MenuBarController;
import linguacrypt.model.Game;
import linguacrypt.model.Player;
import linguacrypt.model.Team;

import java.util.function.Consumer;

public class EditTeamView implements Observer {

    @FXML
    private VBox RedTeam;
    @FXML
    private VBox BlueTeam;
    @FXML
    private Button launchGameButton;

    private Consumer<ActionEvent> onStartGame;
    private Game game;
    public void setGame(Game game) {
        this.game = game;
        game.ajouterObservateur(this);
        reagir();
    }
    @FXML
    private void initialize() {
        launchGameButton.setOnAction(e -> {
            if (onStartGame != null) onStartGame.accept(e);
        });
    }
    public void setOnStartGame(Consumer<javafx.event.ActionEvent> onStartGame) {
        this.onStartGame = onStartGame;
    }


    public void reagir() {
        updateTeamDisplay(game.getRedTeam(), RedTeam, "Rouge");
        updateTeamDisplay(game.getBlueTeam(), BlueTeam, "Bleue");
    }

    private void updateTeamDisplay(Team team, VBox teamBox, String teamName) {
        teamBox.getChildren().clear();

        Label teamTitle = new Label("Équipe " + teamName);
        teamTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        teamBox.getChildren().add(teamTitle);

        for (Player player : team.getPlayers()) {
            HBox playerCard = createPlayerCard(player);
            teamBox.getChildren().add(playerCard);
        }
    }

    private HBox createPlayerCard(Player player) {
        HBox card = new HBox();
        card.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1; -fx-padding: 5;");
        card.setSpacing(10);

        Label playerName = new Label(player.getName());
        playerName.setStyle("-fx-font-size: 12;");
        card.getChildren().add(playerName);

        return card;
    }


}
