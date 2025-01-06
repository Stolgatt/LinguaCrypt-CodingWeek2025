package linguacrypt.view;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import linguacrypt.controller.GameController;
import linguacrypt.model.Card;
import linguacrypt.model.Grid;
import linguacrypt.model.Game;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GameView implements Observer {

    @FXML
    private GridPane gameGrid; // Lié à game_view.fxml
    @FXML
    private Button btnNextTurn;

    private Game game;
    private Runnable onNextTurn;
    private BiConsumer<Integer, Integer> onCardClicked;

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
                cardButton.setOnAction(e -> {if (onCardClicked != null) onCardClicked.accept(finalRow, finalCol);});

                gameGrid.add(cardButton, col, row);
            }
        }
        game.notifierObservateurs();
    }



    public void setOnNextTurn(Runnable onNextTurn) {
        this.onNextTurn = onNextTurn;
    }
    public void setonCardClicked(BiConsumer<Integer, Integer> onCardClicked) {
        this.onCardClicked = onCardClicked;
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
        if (game.isTurnBegin()){
            drawSpyDialogueBox();
        }
    }

    public void drawSpyDialogueBox() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Spy Dialogue");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        TextField wordField = new TextField();
        wordField.setPromptText("Entrez un mot");

        TextField numberField = new TextField();
        numberField.setPromptText("Entrez un entier positif");

        grid.add(new Label("Mot:"), 0, 0);
        grid.add(wordField, 1, 0);
        grid.add(new Label("Entier positif:"), 0, 1);
        grid.add(numberField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                String word = wordField.getText();
                String numberText = numberField.getText();
                try {
                    int number = Integer.parseInt(numberText);
                    if (word.trim().isEmpty() || word.contains(" ")) {
                        showError("Le mot doit être unique et sans espaces.");
                        return null;
                    }
                    if (number > 0) {
                        game.setCurrentHint(word);
                        game.setCurrentNumberWord(number);
                        game.setTurnBegin(false);
                        return null;
                    } else {
                        showError("Le nombre doit être un entier positif.");
                        return null;
                    }
                } catch (NumberFormatException e) {
                    showError("Veuillez entrer un entier valide.");
                }
            }
            return null;
        });
        dialog.showAndWait();
        game.notifierObservateurs();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
