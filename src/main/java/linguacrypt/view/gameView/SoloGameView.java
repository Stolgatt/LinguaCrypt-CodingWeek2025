package linguacrypt.view.gameView;

import javafx.fxml.FXML;
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
import linguacrypt.controller.MenuBarController;
import linguacrypt.controller.TimerController;
import linguacrypt.model.*;
import linguacrypt.model.game.Grid;
import linguacrypt.model.players.AI.AIAgent;
import linguacrypt.view.DialogBox.EndGameDialog;
import linguacrypt.view.DialogBox.EndOfTurnDialog;
import linguacrypt.view.MenuBarView;
import linguacrypt.view.Observer;

import java.util.function.BiConsumer;

public class SoloGameView implements Observer {
    private Game game;
    Font customFont = Font.loadFont(GameViewUtils.class.getResourceAsStream("/fonts/cardFont.otf"), 14);

    @FXML private GridPane gameGrid; // Lié à game.fxml
    @FXML private Button btnNextTurn;
    @FXML private Label labelHint;
    @FXML private Button btnGuess;
    @FXML private MenuBarView menuBarController;
    @FXML private Label timerLabel;
    @FXML private TextField hintFieldSolo;
    @FXML private TextField countFieldSolo;
    @FXML private ImageView imageViewBlueInfo,imageTest;
    @FXML private Label nbMotBlueRestant;
    @FXML private Label BlueSpy;
    @FXML private Label BlueAgent;
    @FXML private Label BlueSpyName;
    @FXML private Label BlueAgentName;

    @FXML private Label whoPlays;
    private TimerController timerController;


    private Dialog<Void> spyDialog;
    private ApplicationContext context = ApplicationContext.getInstance();
    private int cardHeight = context.cardHeight;
    private int cardWidth = context.cardWidth;
    private Runnable onNextTurn;
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
        try {
            MenuBarController menuBarController = new MenuBarController(game);
            this.menuBarController.setController(menuBarController);
        } catch (Exception e) {
            e.printStackTrace();
        }
        game.ajouterObservateur(this);
        GameViewUtils.initializeWordGrid(gameGrid,game.getGrid(),onCardClicked);
        reagir();
    }
    public void setOnNextTurn(Runnable onNextTurn) {
        this.onNextTurn = onNextTurn;
    }
    private BiConsumer<String, String> onGiveHint;
    public void setOnGiveHint(BiConsumer<String, String> onGiveHint) {
        this.onGiveHint = onGiveHint;
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
            btnGuess.setVisible(false);
            if (onGiveHint != null) onGiveHint.accept(hintFieldSolo.getText(), countFieldSolo.getText());
        });
    }


    public void reagir() {
        if (game.getgConfig().getGameMode()!=2){return;}
        if(timerController != null){

            timerController.updateLabel();
        }
        Grid grid = game.getGrid();
        Node root = context.getSoloGameNode();

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
                cardLabel.setTranslateY(cardHeight/5);
                cardLabel.setFont(customFont);
                cardButton.setGraphic(stackPane);
                cardButton.setContentDisplay(ContentDisplay.CENTER);
            }
        }

        drawUI();
        whoPlays.setFont(Font.font(customFont.getName(), customFont.getSize() + 20));
        whoPlays.setTextFill(Color.WHITE);

        if (game.getBlueTeam().getPlayers().getFirst().getIsSpy()){
            btnNextTurn.setVisible(false);
            btnGuess.setVisible(true);
            hintFieldSolo.setVisible(true);
            countFieldSolo.setVisible(true);
            String message = ((AIAgent) game.getBlueTeam().getPlayers().get(1)).guessToString();
            if (message.isEmpty()){
                whoPlays.setText("L'agent a passé son tour.");
            }
            else{
                whoPlays.setText("L'agent a deviné : " +message);
            }
        }
        else{
            btnNextTurn.setVisible(true);
            btnGuess.setVisible(false);
            hintFieldSolo.setVisible(false);
            countFieldSolo.setVisible(false);
            whoPlays.setText("L'espion vous a donné comme indice : " + game.hintToString());
        }

        //check if game is over
        if (game.getIsWin()!=-1){
            context.getEndGameView().showGameResult();
            context.getRoot().setCenter(context.getEndGameNode());
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
    public TimerController getTimerController() {return timerController;}
    public void drawUI(){
        imageViewBlueInfo.setImage(blueBack);
        imageTest.setImage(blueBack);
        imageViewBlueInfo.setVisible(true);
        imageViewBlueInfo.setFitWidth(100);
        imageTest.setFitWidth(100);
        imageViewBlueInfo.setFitHeight(50);
        imageTest.setFitHeight(50);
        imageViewBlueInfo.setPreserveRatio(false);
        imageTest.setPreserveRatio(false);
        imageViewBlueInfo.setSmooth(true);

        String spyName = "";
        String agentName = "";
        if (game.getBlueTeam().getPlayers().getFirst().getIsSpy()){
            spyName = game.getBlueTeam().getPlayers().getFirst().getName();
            agentName = game.getBlueTeam().getPlayers().get(1).getName();
        }
        else{
            spyName = game.getBlueTeam().getPlayers().get(1).getName();
            agentName = game.getBlueTeam().getPlayers().get(0).getName();
        }


        BlueSpy.setFont(Font.font(customFont.getName(), customFont.getSize() + 8));
        BlueAgent.setFont(Font.font(customFont.getName(), customFont.getSize() + 8));

        BlueSpyName.setFont(customFont);
        BlueSpyName.setTextFill(Color.WHITE);
        BlueSpyName.setText(spyName);

        BlueAgentName.setFont(customFont);
        BlueAgentName.setTextFill(Color.WHITE);
        BlueAgentName.setText(agentName);


        nbMotBlueRestant.setFont(customFont);
        nbMotBlueRestant.setTextFill(Color.WHITE);
        nbMotBlueRestant.setText(game.getBlueRemaining() + "");

    }


}
