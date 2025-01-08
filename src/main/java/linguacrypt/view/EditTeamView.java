package linguacrypt.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import linguacrypt.model.Game;
import linguacrypt.model.Player;
import linguacrypt.model.Team;
import linguacrypt.model.statistique.PlayerStat;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class EditTeamView implements Observer {

    @FXML
    private VBox BlueTeamPlayer;
    @FXML
    private VBox RedTeamPlayer;
    @FXML
    private VBox RedTeam;
    @FXML
    private VBox BlueTeam;
    
    @FXML
    private Button launchGameButton;
    @FXML
    private Button addPlayerBtn;
    
    
    @FXML
    private Label teamBlueTitle;
    @FXML
    private Label teamRedTitle;

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
            if (!game.getBlueTeam().isValid() || !game.getRedTeam().isValid()) {
                showError("Les 2 équipes doivent avoir au moins un Espion et un Agent.");
                return;
            }
            if (onStartGame != null) onStartGame.accept(e);
        });
        addPlayerBtn.setOnAction(e -> {
            drawAddPlayerDialogueBox();
        });
        
    }

    public void setOnStartGame(Consumer<javafx.event.ActionEvent> onStartGame) {
        this.onStartGame = onStartGame;
    }

    public void reagir() {
        updateTeamDisplay(game.getBlueTeam(), BlueTeamPlayer);
        updateTeamDisplay(game.getRedTeam(), RedTeamPlayer);
    }

    private void updateTeamDisplay(Team team, VBox teamBox) {
        if (team.getColor() == 0){
            teamBlueTitle.setText(team.getName());
            teamBlueTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            teamBlueTitle.setAlignment(javafx.geometry.Pos.CENTER);
        }
        else{
            teamRedTitle.setText(team.getName());
            teamRedTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
            teamRedTitle.setAlignment(javafx.geometry.Pos.CENTER);
        }
        teamBox.getChildren().clear();
        teamBox.setSpacing(10);

        for (Player player : team.getPlayers()) {
            HBox playerCard = createPlayerCard(player);

            // Définir le style de la carte en fonction de l'équipe
            if (team.getColor() == 0) {
                playerCard.setStyle("-fx-background-color: lightBlue; " +
                        "-fx-border-color: darkGray; " +
                        "-fx-border-width: 2; " +
                        "-fx-padding: 10; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-radius: 10;");
            } else {
                playerCard.setStyle("-fx-background-color: lightCoral; " +
                        "-fx-border-color: darkGray; " +
                        "-fx-border-width: 2; " +
                        "-fx-padding: 10; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-radius: 10;");
            }

            playerCard.setSpacing(15);
            HBox.setMargin(playerCard, new Insets(20));
            playerCard.setAlignment(Pos.CENTER_LEFT);

            teamBox.getChildren().add(playerCard);
        }
    }

    private HBox createPlayerCard(Player player) {
        HBox card = new HBox();

        //playerName
        Label playerName = new Label(player.getName());
        playerName.setStyle("-fx-font-size: 14; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: #333333; " +
                "-fx-padding: 5 10 5 5;");

        //Spy or Agent pic
        ImageView imageRole = new ImageView(new Image(getClass().getResourceAsStream("/image/agent.png")));
        if (player.getIsSpy()){
            imageRole = new ImageView(new Image(getClass().getResourceAsStream("/image/spy.png")));
        }
        imageRole.setFitWidth(20);
        imageRole.setFitHeight(20);

        card.getChildren().addAll(playerName, imageRole);
        return card;
    }

    private void drawAddPlayerDialogueBox() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un Joueur");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        //Champ pour le nom
        TextField nameField = new TextField();
        nameField.setPromptText("Entrez le nom du joueur");

        //Choix de l'équipe
        ToggleGroup teamGroup = new ToggleGroup();
        RadioButton blueTeamRadioButton = new RadioButton("Équipe Bleue");
        blueTeamRadioButton.setToggleGroup(teamGroup);
        blueTeamRadioButton.setSelected(true); // Default = Blue
        RadioButton redTeamRadioButton = new RadioButton("Équipe Rouge");
        redTeamRadioButton.setToggleGroup(teamGroup);

        //Choix du rôle
        ToggleGroup roleGroup = new ToggleGroup();
        RadioButton spyRadioButton = new RadioButton("Rôle Espion");
        spyRadioButton.setToggleGroup(roleGroup);
        spyRadioButton.setSelected(true); // Default = Spy
        RadioButton agentRadioButton = new RadioButton("Rôle Agent");
        agentRadioButton.setToggleGroup(roleGroup);

        // Ajout des éléments dans le GridPane
        grid.add(new Label("Nom du joueur:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Choisir une équipe:"), 0, 1);
        grid.add(blueTeamRadioButton, 1, 1);
        grid.add(redTeamRadioButton, 1, 2);
        grid.add(new Label("Choisir un rôle:"), 0, 3);
        grid.add(spyRadioButton, 1, 3);
        grid.add(agentRadioButton, 1, 4);

        dialog.getDialogPane().setContent(grid);

        //Bouton de Validation/Annulation
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                String playerName = nameField.getText();
                //Verification si un joueur du meme nom appartient déjà à une équipe
                boolean alreadyExists = false;
                for (int i = 0; i<2; i++){
                    Team team = game.getTeam(i);
                    for (Player player : team.getPlayers()) {
                        if (player.getName().equals(playerName)) {
                            alreadyExists = true;
                            break;
                        }
                    }
                }
                if (alreadyExists) {
                    showError("Le joueur " + playerName + " a déjà était ajouté à une équipe");
                    return null;
                }
                //Check si le nom est valide
                if (playerName.trim().isEmpty()) {
                    showError("Le nom du joueur ne peut pas être vide.");
                    return null;
                }
                //Recupere les autre Champs
                boolean isBlueTeam = blueTeamRadioButton.isSelected();
                boolean isSpy = spyRadioButton.isSelected();


                ArrayList<Player> playerList = game.getgConfig().getPlayerList();
                if (playerList == null){
                    playerList = new ArrayList<>();
                }
                boolean find = false;
                for (Player p : playerList){
                    if (p.getName().equals(playerName)) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Profil existant");
                        alert.setHeaderText("Le joueur existe déjà !");
                        alert.setContentText("Un joueur portant ce nom existe déjà. Voulez-vous utiliser ce profil existant ?");

                        ButtonType yesButton = new ButtonType("Oui", ButtonBar.ButtonData.YES);
                        ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.NO);
                        alert.getButtonTypes().setAll(yesButton, noButton);

                        alert.showAndWait().ifPresent(response -> {
                            if (response == yesButton) {
                                tryAddPlayer( isSpy,isBlueTeam, p);
                            }
                        });
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    Player newPlayer = new Player(playerName,isSpy,"",new PlayerStat());
                    tryAddPlayer(isSpy,isBlueTeam, newPlayer);
                }
            }
            game.notifierObservateurs();
            return null;
        });
        dialog.showAndWait();
    }

    public void tryAddPlayer(boolean isSpy,boolean isBlueTeam,Player newPlayer){
        if ( isSpy && ((isBlueTeam && game.getBlueTeam().checkIfHaveSpy()) || (!isBlueTeam && game.getRedTeam().checkIfHaveSpy()))) {
            showError("L'équipe a déjà un espion.");
            return;
        }
        newPlayer.setRole(isSpy);
        int added = 0;
        if (isBlueTeam) {
            try {
                added = game.addPlayer(0,newPlayer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            try {
                added = game.addPlayer(1,newPlayer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if (added == -1){
            showError("L'équipe est déjà pleine.");
        }
    }
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
