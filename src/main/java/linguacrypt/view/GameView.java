package linguacrypt.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import linguacrypt.controller.GameController;
import linguacrypt.model.Card;
import linguacrypt.model.Grid;
import linguacrypt.model.Game;

import java.util.function.Consumer;

public class GameView implements Observer {

    @FXML
    private GridPane gameGrid; // Lié à game_view.fxml
    @FXML
    private Button btnNextTurn;

    private Game game;
    private Runnable onNextTurn;

    @FXML
    private void initialize() {
        btnNextTurn.setOnAction(e -> {
            if (onNextTurn != null) onNextTurn.run();
        });
    }
    public void setGame(Game game) {
        this.game = game;
        game.ajouterObservateur(this);
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
        game.notifierObservateurs();
    }

    private void handleCardClick(int row, int col) {
        Card card = game.getGrid().getCard(row, col);

        // Simule la révélation de la carte (affichage différent dans l'UI)
        game.flipCard(row,col);
        game.notifierObservateurs();
    }

    public void setOnNextTurn(Runnable onNextTurn) {
        this.onNextTurn = onNextTurn;
    }

    public void reagir(){
        int turn = game.getTurn();
        Grid grid = game.getGrid();
        BorderPane root = (BorderPane) gameGrid.getScene().getRoot(); // Récupérer le BorderPane racine de la scène

        switch (turn) {
            case 0:
                root.setStyle("-fx-background-color: rgba(173, 216, 230, 0.5);");  // LightBlue avec alpha 0.5
                break;
            case 1:
                root.setStyle("-fx-background-color: rgba(240, 128, 128, 0.5);");  // LightCoral avec alpha 0.5
                break;
            default:
                root.setStyle("-fx-background-color: rgba(211, 211, 211, 0.5);");  // LightGray avec alpha 0.5
                break;
        }
        for (int row = 0; row < grid.getGrid().length; row++) {
            for (int col = 0; col < grid.getGrid()[row].length; col++) {
                Button cardButton = (Button) gameGrid.getChildren().get(row * grid.getGrid().length + col);
                if (grid.getCard(row,col).isSelected()){
                    switch (grid.getCard(row,col).getCouleur()){
                        case 0:
                            cardButton.setStyle("-fx-background-color: #F5DEB3;");  // White
                            break;
                        case 1:
                            cardButton.setStyle("-fx-background-color: lightblue;");   // Blue
                            break;
                        case 2:
                            cardButton.setStyle("-fx-background-color: lightcoral;");    // Red
                            break;
                        case 3:
                            cardButton.setStyle("-fx-background-color: darkgray;");  // Black
                            break;
                    }
                }

            }
        }
    }


}
