package linguacrypt.view;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.Player;
import linguacrypt.model.statistique.PlayerStat;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ProfileMenuView implements Observer {


    @FXML
    private Label test;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private VBox statsBox;
    @FXML
    private Button MainMenuBtn;

    private Game game;
    private Consumer<ActionEvent> onMenuPrincipal;

    @FXML
    public void initialize() {
        MainMenuBtn.setOnAction(e -> {onMenuPrincipal.accept(e);});

        ArrayList<String> playerNames = new ArrayList<>();
        for (Player p : GameConfiguration.getInstance().getPlayerList()) {
            playerNames.add(p.getName());
        }
        comboBox.setItems(FXCollections.observableArrayList(playerNames));
        comboBox.setOnAction(event -> reagir());
    }

    public void setGame(Game game) {
        System.out.println("ALloooo\n");
        this.game = game;
        game.ajouterObservateur(this);
        reagir();
    }
    public void setOnMenuPrincipal(Consumer<ActionEvent> onMenuPrincipal) {this.onMenuPrincipal = onMenuPrincipal;}

    public void reagir() {
        String playerName = comboBox.getValue();
        if (playerName == null) {
            return;
        }

        // Recherche du joueur correspondant au nom sélectionné.
        Player selectedPlayer = null;
        for (Player p : GameConfiguration.getInstance().getPlayerList()) {
            if (p.getName().equals(playerName)) {
                selectedPlayer = p;
                break;
            }
        }

        if (selectedPlayer != null) {
            // Obtention des statistiques du joueur.
            PlayerStat stats = selectedPlayer.getStat();

            // Nettoyage du conteneur avant d'ajouter les nouvelles statistiques.
            statsBox.getChildren().clear();

            // Ajout des statistiques sous forme de Labels.
            statsBox.getChildren().addAll(
                    new Label("Statistiques de : " + selectedPlayer.getName()),
                    new Label("Parties jouées : " + stats.getPartiesJouees()),
                    new Label("Victoires : " + stats.getVictoires()),
                    new Label("Défaites : " + stats.getDefaites()),
                    new Label("Ratio Victoires/Défaites : " + String.format("%.2f", stats.getRatioVictoiresDefaites())),
                    new Label("Temps total joué : " + stats.getTempsTotal() / 60 + " minutes"),
                    new Label("Rôle favori : " + ((stats.getNbPartieJoueEspion() > stats.getPartiesJouees() - stats.getNbPartieJoueEspion()) ? "Espion" : "Agent"))
            );
        }
    }
}
