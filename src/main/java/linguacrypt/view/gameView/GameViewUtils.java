package linguacrypt.view.gameView;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import linguacrypt.model.game.Card;
import linguacrypt.model.game.Grid;

import java.util.function.BiConsumer;

public class GameViewUtils {
    public static void initializeWordGrid(GridPane gameGrid, Grid grid, BiConsumer<Integer, Integer> onCardClicked){
        // Parcours et affichage des cartes dans la grille
        for (int row = 0; row < grid.getGrid().length; row++) {
            for (int col = 0; col < grid.getGrid()[row].length; col++) {
                Card card = grid.getCard(row, col);
                Button cardButton = new Button(card.getWord());
                cardButton.setPrefSize(100, 50);

                // Ajout d'un événement clic
                int finalRow = row;
                int finalCol = col;
                cardButton.setOnAction(e -> {if (onCardClicked != null) onCardClicked.accept(finalRow, finalCol);});

                gameGrid.add(cardButton, col, row);
            }
        }
    }
}
