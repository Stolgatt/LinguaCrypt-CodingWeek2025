package linguacrypt.view.gameView;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import linguacrypt.ApplicationContext;
import linguacrypt.controller.TimerController;
import linguacrypt.model.*;
import linguacrypt.model.Game;
import linguacrypt.model.game.Grid;
import linguacrypt.model.game.Card;
import linguacrypt.view.DialogBox.EndGameDialog;
import linguacrypt.view.DialogBox.EndOfTurnDialog;
import linguacrypt.view.MenuBarView;
import linguacrypt.view.Observer;

import java.util.function.BiConsumer;

public class SoloGameView implements Observer {
    private Game game;
    Font customFont = Font.loadFont(GameViewUtils.class.getResourceAsStream("/fonts/cardFont.otf"), 14);

    @FXML private GridPane gameGrid; // Lié à game.fxml
    @FXML private VBox motTrouve;
    @FXML private Button btnNextTurn;
    @FXML private Label labelHint;
    @FXML private Button btnGuess;
    @FXML private MenuBarView menuBarController;
    @FXML private Label timerLabel;
    @FXML private TextField hintField;
    @FXML private TextField countField;

    private TimerController timerController;

    private Dialog<Void> spyDialog;
    private ApplicationContext context = ApplicationContext.getInstance();
    private int cardHeight = context.cardHeight;
    private int cardWidth = context.cardWidth;
    private Runnable onNextTurn;
    private Runnable OnGiveHint;
    private BiConsumer<Integer, Integer> onCardClicked;

    Image frontWhite = new Image(getClass().getResourceAsStream("/image/front_white.png"));
    Image frontBlue = new Image(getClass().getResourceAsStream("/image/front_blue.png"));
    Image frontRed = new Image(getClass().getResourceAsStream("/image/front_red.png"));
    Image frontBlack = new Image(getClass().getResourceAsStream("/image/front_black.png"));

    Image redBack = new Image(getClass().getResourceAsStream("/image/backs_part_4.png"));
    Image blueBack = new Image(getClass().getResourceAsStream("/image/backs_part_1.png"));
    Image whiteBack = new Image(getClass().getResourceAsStream("/image/backs_part_2.png"));
    Image blackBack = new Image(getClass().getResourceAsStream("/image/black-back.png"));

    public void setGame(Game game) {
        this.game = game;
        game.ajouterObservateur(this);
        GameViewUtils.initializeWordGrid(gameGrid,game.getGrid(),onCardClicked);
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


    public void reagir() {
        if (game.getgConfig().getGameMode()!=2){return;}
        if(timerController != null){

            timerController.updateLabel();
        }
        motTrouve.getChildren().clear();
        Grid grid = game.getGrid();
        Node root = context.getGameNode();

        //BACKGROUND COLOR
        root.setStyle("-fx-background-image: url('image/bg_blue.jpg'); -fx-background-size: cover;");
        //DRAW GRID
        for (int row = 0; row < grid.getGrid().length; row++) {
            for (int col = 0; col < grid.getGrid()[row].length; col++) {
                Button cardButton = (Button) gameGrid.getChildren().get(row * grid.getGrid().length + col);

                Label cardLabel = new Label(grid.getCard(row, col).getWord());
                Image image = null;

                if (grid.getCard(row,col).isSelected()) {
                    cardLabel.setText("");
                    image = switch (grid.getCard(row, col).getCouleur()) {
                        case 0 -> whiteBack;
                        case 1 -> blueBack;
                        case 2 -> redBack;
                        case 3 -> blackBack;
                        default -> null;
                    };
                }
                else if(game.getBlueTeam().getPlayers().getFirst().getIsSpy()){
                        cardLabel.setText(grid.getCard(row, col).getWord());

                        if (grid.getCard(row, col).getCouleur() == 3){
                            cardLabel.setTextFill(Color.WHITE);
                        }
                        image = switch (grid.getCard(row, col).getCouleur()) {
                            case 0 -> frontWhite;
                            case 1 -> frontBlue;
                            case 2 -> frontRed;
                            case 3 -> frontBlack;
                            default -> null;
                        };
                        motTrouve.getChildren().add(cardLabel);
                }
                else{
                    image = frontWhite;
                }
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(cardWidth);
                imageView.setFitHeight(cardHeight);
                imageView.setPreserveRatio(false);
                imageView.setSmooth(true);

                StackPane stackPane = new StackPane();
                stackPane.setPrefSize(cardWidth, cardHeight);
                stackPane.getChildren().addAll(imageView, cardLabel);
                cardLabel.setTranslateY(10);
                cardLabel.setFont(customFont);
                cardButton.setGraphic(stackPane);
                cardButton.setContentDisplay(ContentDisplay.CENTER);
            }
        }

        if (game.getBlueTeam().getPlayers().getFirst().getIsSpy()){
            btnNextTurn.setVisible(false);
            btnGuess.setVisible(true);
            hintField.setVisible(true);
            countField.setVisible(true);
        }
        else{
            btnNextTurn.setVisible(true);
            btnGuess.setVisible(false);
            hintField.setVisible(false);
            countField.setVisible(false);
        }

        //draw the actual hint
        String message = game.hintToString();
        labelHint.setText("Indice pour ce tour : " + message);

        //check if game is over
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
                        GameViewUtils.showError("Le mot doit être unique et sans espaces.");
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
                        GameViewUtils.showError("Le nombre doit être un entier positif.");
                        return null;
                    }
                } catch (NumberFormatException e) {
                    GameViewUtils.showError("Veuillez entrer un entier valide.");
                }
            }
            return null;
        });
        spyDialog.showAndWait();
        game.notifierObservateurs();
    }

    public TimerController getTimerController() {return timerController;}



}
