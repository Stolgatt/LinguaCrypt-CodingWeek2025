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
import linguacrypt.model.Player.Player;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ProfileMenuView implements Observer {


    @FXML
    private Label statTitle;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private VBox statsBox;
    @FXML
    private Button MainMenuBtn;
    @FXML
    private Label partiesJoueesLabel;
    @FXML
    private Label victoiresLabel;
    @FXML
    private Label defaitesLabel;
    @FXML
    private Label ratioVDLabel;
    @FXML
    private Label tempsTotalLabel;
    @FXML
    private Label rolePrefereLabel;

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
        statTitle.setText("Statistiques du Joueur "+playerName);
        // Récupération des statistiques du joueur.
        Player selectedPlayer = GameConfiguration.getInstance().getPlayerList().stream()
                .filter(player -> player.getName().equals(playerName))
                .findFirst()
                .orElse(null);

        if (selectedPlayer != null) {
            partiesJoueesLabel.setText(String.valueOf(selectedPlayer.getStat().getPartiesJouees()));
            victoiresLabel.setText(String.valueOf(selectedPlayer.getStat().getVictoires()));
            defaitesLabel.setText(String.valueOf(selectedPlayer.getStat().getDefaites()));
            ratioVDLabel.setText(String.format("%.2f", selectedPlayer.getStat().getRatioVictoiresDefaites()));
            tempsTotalLabel.setText(String.valueOf(selectedPlayer.getStat().getTempsTotal() / 60)); // Convertir en minutes
            rolePrefereLabel.setText((selectedPlayer.getStat().getNbPartieJoueEspion() >
                    selectedPlayer.getStat().getPartiesJouees() - selectedPlayer.getStat().getNbPartieJoueEspion()) ? "Espion" : "Agent");
        }
    }
}
