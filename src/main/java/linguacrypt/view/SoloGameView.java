package linguacrypt.view;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import linguacrypt.ApplicationContext;
import linguacrypt.controller.TimerController;
import linguacrypt.model.*;
import linguacrypt.model.Game;
import linguacrypt.model.game.Grid;
import linguacrypt.model.game.Card;
import linguacrypt.view.DialogBox.EndGameDialog;
import linguacrypt.view.DialogBox.EndOfTurnDialog;

import java.util.function.BiConsumer;

public class SoloGameView implements Observer {
    private Game game;

    @FXML
    private GridPane gameGrid; // Lié à game.fxml
    @FXML
    private VBox motTrouve;
    @FXML
    private Button btnNextTurn;
    @FXML
    private Label labelHint;
    @FXML
    private Button btnGuess;
    @FXML
    private MenuBarView menuBarController;
    @FXML
    private Label timerLabel;

    private TimerController timerController;

    private Dialog<Void> spyDialog;
    private ApplicationContext context = ApplicationContext.getInstance();
    private Runnable onNextTurn;
    private Runnable OnGiveHint;
    private BiConsumer<Integer, Integer> onCardClicked;


    public void setGame(Game game) {
        this.game = game;
        game.ajouterObservateur(this);
        initializeGrid(game.getGrid());
        reagir();
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
    @FXML
    private void initialize() {
        btnNextTurn.setOnAction(e -> {
            if (onNextTurn != null) onNextTurn.run();
            resetTimer();
        });
        btnGuess.setOnAction(e -> {
            if (OnGiveHint != null) OnGiveHint.run();
        });
    }

    private void initializeGrid(Grid grid){
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

    public void reagir() {
        if (game.getgConfig().getGameMode()!=2){return;}
        if(timerController != null){

            timerController.updateLabel();
        }
        motTrouve.getChildren().clear();
        int turn = game.getTurn();
        Grid grid = game.getGrid();
        Node root = context.getGameNode();

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

                if (grid.getCard(row,col).isSelected() || game.getBlueTeam().getPlayers().getFirst().getIsSpy()){
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
                    if(game.getBlueTeam().getPlayers().getFirst().getIsSpy()){
                        Card discoveredCard = game.getGrid().getCard(row,col);
                        if (!discoveredCard.isSelected()){continue;}
                        if (discoveredCard != null) {
                            Label cardLabel = new Label(discoveredCard.getWord());
                            if (discoveredCard.getCouleur()==1){
                                cardLabel.setStyle("-fx-background-color: lightblue; -fx-border-color: black; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5;");
                            }
                            else{
                                cardLabel.setStyle("-fx-background-color: #F5DEB3; -fx-border-color: black; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5;");

                            }
                            cardLabel.setPrefSize(100, 30);
                            cardLabel.setAlignment(Pos.CENTER);

                            motTrouve.getChildren().add(cardLabel);
                        }
                    }
                }
                else{
                    cardButton.setStyle("");
                }
            }
        }
        if (game.getBlueTeam().getPlayers().getFirst().getIsSpy()){
            btnNextTurn.setVisible(false);
        }
        else{
            btnGuess.setVisible(false);
        }
        //Check if button can be visible or not

        if (game.isTurnBegin()==1){
            if (game.getBlueTeam().getPlayers().getFirst().getIsSpy()){
                drawSpyDialogueBox();
            }
        }
        /*
        else if (game.isTurnBegin()==2){
            btnGuess.setVisible(false);
            btnNextTurn.setVisible(true);
        }
        else{
            btnNextTurn.setVisible(false);
            btnGuess.setVisible(true);
        }*/

        //draw the actual hint
        String message = game.hintToString();
        labelHint.setText("Indice pour ce tour : " + message);

        //check if game is over
        /*
        if (game.getIsWin() != -1 && game.getIsWin() != 2){
            timerController.stopTimer();
            drawWinningDialogueBox();
            return;
        }
        if (game.getIsWin() == 2){
            timerController.stopTimer();
            drawLoosingDialogueBox();
        }
         */
        if (game.getIsWin()!=-1){
            EndGameDialog.showEndGameDialog(game);
        }
    }


    //A MODIFIER (ON PEUT FACTORISER DES METHODES AVEC GAMEVIEW)

    public void setTimer(){
        // initialize timer controller
        int timeTurn = GameConfiguration.getInstance().getTimeTurn();
        if (timeTurn > 0) {
            timerController = new TimerController(timerLabel, timeTurn);
            timerController.setOnTimerEnd(this::handleTimerEnd);
            timerController.startTimer();
        } else {
            timerController = new TimerController(timerLabel, timeTurn);
            timerLabel.setText("∞"); // Prints infinity if time isn't limited
        }
    }
    public void resetTimer() {
        int timeTurn = GameConfiguration.getInstance().getTimeTurn();

        if (timeTurn > 0) {
            timerController.resetTimer(timeTurn);
            timerController.startTimer();
        } else {
            timerLabel.setText("∞"); // Display infinity symbol if time is unlimited
        }
    }

    /**
     * Handles the actions to be performed when the timer ends.
     * This includes closing any open dialogs, managing turn transitions,
     * and resetting the timer for the next turn.
     */
    public void handleTimerEnd() {
        // Get the current state of the turn (spy or agent phase)
        int turnState = game.isTurnBegin();

        // Show the end-of-turn dialog and execute the specified actions afterward
        EndOfTurnDialog.showEndOfTurnDialog(() -> {
            if (spyDialog != null) {
                spyDialog.close();
                spyDialog = null;
            }
            // Handle actions based on the current turn state
            if (turnState == 0 || turnState == 1) { // Spy's turn
                //nextTurn();
            } else if (turnState == 2) { // Agents' turn
                //nextTurn();
            }
            resetTimer();
        });
    }

    public void drawSpyDialogueBox() {
        spyDialog = new Dialog<>();
        spyDialog.setTitle("Spy Dialogue");

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

        spyDialog.getDialogPane().setContent(grid);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        spyDialog.getDialogPane().getButtonTypes().addAll(okButtonType);

        spyDialog.setResultConverter(dialogButton -> {
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
                        System.out.println("WOUH");
                        onNextTurn.run();
                        resetTimer();
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
        spyDialog.showAndWait();
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

    public TimerController getTimerController() {return timerController;}


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
