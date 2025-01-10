package linguacrypt.view.gameView;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import linguacrypt.ApplicationContext;
import linguacrypt.model.game.Card;
import linguacrypt.model.game.Grid;

import java.util.function.BiConsumer;

public class GameViewUtils {

    public static void initializeWordGrid(GridPane gameGrid, Grid grid, BiConsumer<Integer, Integer> onCardClicked){
        // Parcours et affichage des cartes dans la grille
        int cardHeight = ApplicationContext.getInstance().cardHeight;
        int cardWidth = ApplicationContext.getInstance().cardWidth;
        for (int row = 0; row < grid.getGrid().length; row++) {
            for (int col = 0; col < grid.getGrid()[row].length; col++) {
                Card card = grid.getCard(row, col);

                // Créer un bouton
                Button cardButton = new Button();
                cardButton.setPrefSize(cardWidth, cardHeight); // Fixe la taille du bouton
                cardButton.setMaxSize(cardWidth, cardHeight);
                cardButton.setMinSize(cardWidth, cardHeight);
                cardButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;"); // Supprime les marges et le fond

                // Créer un Label pour afficher le texte
               // Label label = new Label(card.getWord());
               // Font customFont = Font.loadFont(GameViewUtils.class.getResourceAsStream("/fonts/cardFont.otf"), 14); // 14 est la taille de la police
               // label.setFont(customFont);
//
               // // Ajouter le Label comme graphique du bouton
               // cardButton.setGraphic(label);

                // Ajouter un événement clic
                int finalRow = row;
                int finalCol = col;
                cardButton.setOnAction(e -> {
                    if (onCardClicked != null) onCardClicked.accept(finalRow, finalCol);
                });

                // Ajouter le bouton à la grille
                gameGrid.add(cardButton, col, row);
            }
        }
    }
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
