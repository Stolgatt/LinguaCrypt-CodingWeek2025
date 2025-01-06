package linguacrypt.view;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXMLLoader;
import linguacrypt.controller.GameController;
import linguacrypt.model.Card;
import linguacrypt.model.Grid;
import linguacrypt.model.Game;
import linguacrypt.controller.MenuBarController;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GameView implements Observer {

    @FXML
    private GridPane gameGrid; // Lié à game_view.fxml
    @FXML
    private Button btnNextTurn;
    @FXML
    private Label labelHint;
    @FXML
    private Button btnGuess;
    @FXML
    private MenuBarView menuBarController;

    private Game game;
    private Runnable onNextTurn;
    private Runnable OnGiveHint;
    private BiConsumer<Integer, Integer> onCardClicked;

    @FXML
    private void initialize() {
        btnNextTurn.setOnAction(e -> {
            btnNextTurn.setVisible(false);
            if (onNextTurn != null) onNextTurn.run();
        });
        btnGuess.setOnAction(e -> {
            btnGuess.setVisible(false);
            if (OnGiveHint != null) OnGiveHint.run();
        });
    }

    public void setGame(Game game) {
        this.game = game;
        game.ajouterObservateur(this);
        
        try {
            MenuBarController menuBarController = new MenuBarController(game);
            this.menuBarController.setController(menuBarController);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
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
    public void setOnGiveHint(Runnable OnGiveHint) {
        this.OnGiveHint = OnGiveHint;
    }
    public void setonCardClicked(BiConsumer<Integer, Integer> onCardClicked) {
        this.onCardClicked = onCardClicked;
    }

    public void reagir(){
        int turn = game.getTurn();
        Grid grid = game.getGrid();
        BorderPane root = (BorderPane) gameGrid.getScene().getRoot();

        //BACKGROUND COLOR
        switch (turn) {
            case 0:
                root.setStyle("-fx-background-color: rgba(173, 216, 230, 0.5);");
                break;
            case 1:
                root.setStyle("-fx-background-color: rgba(240, 128, 128, 0.5);");
                break;
            default:
                root.setStyle("-fx-background-color: rgba(211, 211, 211, 0.5);");
                break;
        }

        //DRAW GRID
        for (int row = 0; row < grid.getGrid().length; row++) {
            for (int col = 0; col < grid.getGrid()[row].length; col++) {
                Button cardButton = (Button) gameGrid.getChildren().get(row * grid.getGrid().length + col);
                // Update button text with loaded word
                cardButton.setText(grid.getCard(row, col).getWord());
                
                if (grid.getCard(row,col).isSelected() || game.isTurnBegin()==0){
                    switch (grid.getCard(row,col).getCouleur()){
                        case 0:
                            cardButton.setStyle("-fx-background-color: #F5DEB3;");
                            break;
                        case 1:
                            cardButton.setStyle("-fx-background-color: lightblue;");
                            break;
                        case 2:
                            cardButton.setStyle("-fx-background-color: lightcoral;");
                            break;
                        case 3:
                            cardButton.setStyle("-fx-background-color: darkgray;");
                            break;
                    }
                }
                else{
                    cardButton.setStyle("");
                }
            }
        }

        //Check if button can be visible or not
        if (game.isTurnBegin()==1){
            drawSpyDialogueBox();
        }
        else if (game.isTurnBegin()==2){
            btnGuess.setVisible(false);
            btnNextTurn.setVisible(true);
        }
        else{
            btnNextTurn.setVisible(false);
            btnGuess.setVisible(true);
        }

        //draw the actual hint
        String message = game.hintToString();
        labelHint.setText("Indice pour ce tour : " + message);

        //check if game is over
        if (game.getIsWin() != -1 && game.getIsWin() != 2){
            drawWinningDialogueBox();
            return;
        }
        if (game.getIsWin() == 2){
            drawLoosingDialogueBox();
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
                        game.setTurnBegin(2);
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

    public void drawWinningDialogueBox() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Winning Dialogue");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        String message = "";
        if (game.isWinning() == 0){
            message = "L'équipe bleue a gagné !";
        }
        else{
            message = "L'équipe rouge a gagné !";
        }
        Label messageLabel = new Label(message);
        grid.add(messageLabel, 0, 0);
        dialog.getDialogPane().setContent(grid);
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType);
        dialog.showAndWait();
    }

    public void drawLoosingDialogueBox() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Loosing Dialogue");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        String message = "";
        if (game.getTurn() == 0){
            message = "L'équipe bleue a perdu ! Elle a trouvé l'assassin ...";
        }
        else{
            message =  "L'équipe rouge a perdu ! Elle a trouvé l'assassin ...";
        }
        Label messageLabel = new Label(message);
        grid.add(messageLabel, 0, 0);
        dialog.getDialogPane().setContent(grid);
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType);
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
