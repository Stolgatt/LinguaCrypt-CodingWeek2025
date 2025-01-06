package linguacrypt.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import linguacrypt.model.Card;
import linguacrypt.model.Grid;
import linguacrypt.model.Game;

public class GameView {

    @FXML
    private GridPane gameGrid; // Lié à game_view.fxml

    private Game game;

    public void setGame(Game game) {
        this.game = game;
        initializeGrid();
    }

    private void initializeGrid() {
        Grid grid = game.getGrid();

        // Parcours et affichage des cartes dans la grille
        for (int row = 0; row < grid.getGrid().length; row++) {
            for (int col = 0; col < grid.getGrid()[row].length; col++) {
                Card card = grid.getCard(row, col);
                Button cardButton = new Button(card.getWord());
                cardButton.setPrefSize(100, 50);

                // Ajout d'un événement clic
                int finalRow = row;
                int finalCol = col;
                cardButton.setOnAction(e -> handleCardClick(finalRow, finalCol));

                gameGrid.add(cardButton, col, row);
            }
        }
    }

    private void handleCardClick(int row, int col) {
        Card card = game.getGrid().getCard(row, col);

        // Simule la révélation de la carte (affichage différent dans l'UI)
        game.flipCard(row,col);
        System.out.println("Card clicked: " + card.getWord() + " - " + card.getCouleur());
    }
}
