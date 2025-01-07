package linguacrypt.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

        for (Player player : team.getPlayers()) {
            HBox playerCard = createPlayerCard(player);
            teamBox.getChildren().add(playerCard);
        }
    }

    private HBox createPlayerCard(Player player) {
        HBox card = new HBox();
        card.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1; -fx-padding: 5;");
        card.setSpacing(10);
        /*
        ImageView imageAvatar = new ImageView(new Image(getClass().getResourceAsStream(player.getUrlAvatar())));
        imageAvatar.setFitWidth(20);
        imageAvatar.setFitHeight(20);
        */
        Label playerName = new Label(player.getName());
        playerName.setStyle("-fx-font-size: 12;");

        ImageView imageRole = new ImageView(new Image(getClass().getResourceAsStream("/image/agent.png")));
        if (player.getIsSpy()){
            imageRole = new ImageView(new Image(getClass().getResourceAsStream("/image/spy.png")));
        }
        imageRole.setFitWidth(20);
        imageRole.setFitHeight(20);

        //card.getChildren().addAll(imageAvatar, playerName, imageRole);
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

        // Champ pour le nom du joueur
        TextField nameField = new TextField();
        nameField.setPromptText("Entrez le nom du joueur");

        ToggleGroup teamGroup = new ToggleGroup();

        RadioButton blueTeamRadioButton = new RadioButton("Équipe Bleue");
        blueTeamRadioButton.setToggleGroup(teamGroup);
        blueTeamRadioButton.setSelected(true); // Par défaut, équipe Bleue

        RadioButton redTeamRadioButton = new RadioButton("Équipe Rouge");
        redTeamRadioButton.setToggleGroup(teamGroup);

        ToggleGroup roleGroup = new ToggleGroup();

        RadioButton spyRadioButton = new RadioButton("Rôle Espion");
        spyRadioButton.setToggleGroup(roleGroup);
        spyRadioButton.setSelected(true); // Par défaut, rôle Espion

        RadioButton agentRadioButton = new RadioButton("Rôle Agent");
        agentRadioButton.setToggleGroup(roleGroup);

        FileChooser imageChooser = new FileChooser();
        imageChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        /*
        Button imageButton = new Button("Choisir une image");
        AtomicReference<File> selectedImage = new AtomicReference<>();
        imageButton.setOnAction(e -> {
            File file = imageChooser.showOpenDialog(dialog.getOwner());
            if (file != null) {
                selectedImage.set(file);
            }
        });

         */

        // Ajout des éléments dans le GridPane
        grid.add(new Label("Nom du joueur:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Choisir une équipe:"), 0, 1);
        grid.add(blueTeamRadioButton, 1, 1);
        grid.add(redTeamRadioButton, 1, 2);
        grid.add(new Label("Choisir un rôle:"), 0, 3);
        grid.add(spyRadioButton, 1, 3);
        grid.add(agentRadioButton, 1, 4);
        //grid.add(imageButton, 1, 5);

        dialog.getDialogPane().setContent(grid);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                String playerName = nameField.getText();
                boolean isBlueTeam = blueTeamRadioButton.isSelected();
                boolean isSpy = spyRadioButton.isSelected();
                //File image = selectedImage.get();

                if (playerName.trim().isEmpty()) {
                    showError("Le nom du joueur ne peut pas être vide.");
                    return null;
                }
                /*
                if (image != null) {
                    try {
                        Path sourcePath = image.toPath();
                        Path targetPath = Paths.get("src/main/resources/image/" + image.getName());
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        int maxAttempts = 10;
                        int attempts = 0;
                        while (!Files.exists(targetPath) && attempts < maxAttempts) {
                            attempts++;
                            Thread.sleep(500); // Attente de 500 ms avant de réessayer
                        }

                        // Si après les tentatives, le fichier n'existe toujours pas, traiter l'erreur
                        if (!Files.exists(targetPath)) {
                            showError("Le fichier image n'a pas été correctement copié.");
                            return null;
                        }
                        image = targetPath.toFile();
                    } catch (IOException ex) {
                        showError("Erreur lors de l'enregistrement de l'image.");
                        return null;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    URL imageUrl = getClass().getResource("/image/default_pic.png");
                    if (imageUrl != null) {
                        try {
                            image = new File(imageUrl.toURI());
                        } catch (URISyntaxException e) {
                            showError("Erreur lors de l'enregistrement de l'image.");
                            return null;
                        }
                    } else {
                        showError("Erreur lors de l'enregistrement de l'image.");
                        return null;
                    }
                    System.out.println("Default pic choose");
                }
                assert image != null;
                String imageUrl = "/image/" + image.getName();
                */
                if ( isSpy && ((isBlueTeam && game.getBlueTeam().checkIfHaveSpy()) || (!isBlueTeam && game.getRedTeam().checkIfHaveSpy()))) {
                    showError("L'équipe a déjà un espion.");
                    return null;
                }
                Player newPlayer = new Player(playerName,isSpy,"");
                int added = 0;
                if (isBlueTeam) {
                    added = game.getBlueTeam().addPlayer(newPlayer);
                }
                else{
                    added = game.getRedTeam().addPlayer(newPlayer);
                }
                if (added == -1){
                    showError("L'équipe est déjà pleine.");
                    return null;
                }
                game.notifierObservateurs();
            }
            return null;
        });
        dialog.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
