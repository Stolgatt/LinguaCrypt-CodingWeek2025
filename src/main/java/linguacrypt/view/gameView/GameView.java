package linguacrypt.view.gameView;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import linguacrypt.ApplicationContext;
import linguacrypt.controller.TimerController;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.game.Grid;
import linguacrypt.model.game.Card;
import linguacrypt.model.Game;

import linguacrypt.controller.MenuBarController;
import linguacrypt.model.players.Player;
import linguacrypt.view.DialogBox.EndGameDialog;
import linguacrypt.view.DialogBox.EndOfTurnDialog;
import linguacrypt.view.MenuBarView;
import linguacrypt.view.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class GameView implements Observer {
    Font customFont = Font.loadFont(GameViewUtils.class.getResourceAsStream("/fonts/cardFont.otf"), 14);
    @FXML private MenuBarView menuBarController;
    @FXML private Label timerLabel;
    @FXML private GridPane gameGrid; // Lié à game.fxml
    @FXML private Button btnNextTurn;
    @FXML private Button btnGuess;
    @FXML private Label whoPlays;
    @FXML private Label labelHint;

    @FXML private ImageView imageViewRedInfo;
    @FXML private Label nbMotRedRestant;
    @FXML private Label RedSpy;
    @FXML private Label RedAgent;
    @FXML private Label RedSpyName;
    @FXML private VBox RedAgentName;
    @FXML private ImageView imageViewBlueInfo;
    @FXML private Label nbMotBlueRestant;
    @FXML private Label BlueSpy;
    @FXML private Label BlueAgent;
    @FXML private Label BlueSpyName;
    @FXML private VBox BlueAgentName;
    @FXML private TextField hintField;
    @FXML private TextField countField;


    private TimerController timerController;

    private Dialog<Void> spyDialog;

    private Game game;
    private ApplicationContext context = ApplicationContext.getInstance();
    private int cardHeight = context.cardHeight;
    private int cardWidth = context.cardWidth;
    private Runnable onNextTurn;
    private BiConsumer<String, String> onGiveHint;
    private BiConsumer<Integer, Integer> onCardClicked;

    Image frontWhite = new Image(getClass().getResourceAsStream("/image/front_white.png"));
    Image frontBlue = new Image(getClass().getResourceAsStream("/image/front_blue.png"));
    Image frontRed = new Image(getClass().getResourceAsStream("/image/front_red.png"));
    Image frontBlack = new Image(getClass().getResourceAsStream("/image/front_black.png"));

    Image redBack = new Image(getClass().getResourceAsStream("/image/backs_part_4.png"));
    Image blueBack = new Image(getClass().getResourceAsStream("/image/backs_part_1.png"));
    Image whiteBack = new Image(getClass().getResourceAsStream("/image/backs_part_2.png"));
    Image blackBack = new Image(getClass().getResourceAsStream("/image/black-back.png"));

    @FXML
    private void initialize() {
        btnNextTurn.setOnAction(e -> {
            btnNextTurn.setVisible(false);
            if (onNextTurn != null) onNextTurn.run();

            resetTimer();
        });
        btnGuess.setOnAction(e -> {
            btnGuess.setVisible(false);
            if (onGiveHint != null) onGiveHint.accept(hintField.getText(), countField.getText());
        });
    }

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

    public void setGame(Game game) {
        this.game = game;
        game.ajouterObservateur(this);

        try {
            MenuBarController menuBarController = new MenuBarController(game);
            this.menuBarController.setController(menuBarController);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Platform.runLater(this::initializeGrid);
    Platform.runLater(this::updateRedTeamVBox);
    Platform.runLater(this::updateBlueTeamVBox);
    }

    private void initializeGrid() {
        Grid grid = game.getGrid();
        
        switch (GameConfiguration.getInstance().getGameMode()) {
            case 0:                                 // Words Game Mode
                GameViewUtils.initializeWordGrid(gameGrid,grid,onCardClicked);
                break;
            case 1:                                 // Picture Game Mode
                initializePictureGrid(grid);
                break;
            default:
                GameViewUtils.initializeWordGrid(gameGrid,grid,onCardClicked);
                break;
        }
        game.notifierObservateurs();
    }

    private void initializePictureGrid(Grid grid) {
        // Parcours et affichage des cartes dans la grille
        for (int row = 0; row < grid.getGrid().length; row++) {
            for (int col = 0; col < grid.getGrid()[row].length; col++) {
                // Récupérer la carte actuelle
                Card card = grid.getCard(row, col);
    
                // Créer une image à partir de l'URL stockée dans la carte
                Image image = new Image("pictures/"+card.getUrlImage());  
                ImageView imageView = new ImageView(image);
    
                // Configurer la taille de l'image
                imageView.setFitWidth(100);

                imageView.setFitHeight(100); // Hauteur fixe
                imageView.setPreserveRatio(true); // Conserver les proportions
    
                // Créer un bouton et ajouter l'image comme contenu
                Button cardButton = new Button();
                cardButton.setGraphic(imageView);
                cardButton.setPrefSize(100, 100); // Taille fixe pour le bouton
    
                // Ajouter un événement clic sur le bouton
                int finalRow = row;
                int finalCol = col;
                cardButton.setOnAction(e -> {
                    if (onCardClicked != null) {
                        onCardClicked.accept(finalRow, finalCol);
                    }
                });
    
                // Ajouter le bouton à la grille
                gameGrid.add(cardButton, col, row);
            }
        }
    }        

    public void setOnNextTurn(Runnable onNextTurn) {
        this.onNextTurn = onNextTurn;
    }
    public void setOnGiveHint(BiConsumer<String, String> onGiveHint) {
        this.onGiveHint = onGiveHint;
    }
    public void setonCardClicked(BiConsumer<Integer, Integer> onCardClicked) {
        this.onCardClicked = onCardClicked;
    }

    public void reagir() {

        // Multiplayer check
        boolean isSpy;
        boolean isMulti;
        if(context.getServer()!= null){
            isMulti = true; 
            isSpy = context.getServer().getServerUser().getPlayer().getIsSpy();
        }else if(context.getClient() != null){
            isMulti = true;
            isSpy = context.getClient().getUser().getPlayer().getIsSpy();
        }else{
            isMulti = false;
            isSpy = false;
        }



        if (game.getgConfig().getGameMode() == 2){return;}
        if(timerController != null){
            timerController.updateLabel();
        }
        updateRedTeamVBox();
        updateBlueTeamVBox();

        int turn = game.getTurn();
        Grid grid = game.getGrid();
        Node root = context.getGameNode();
        //BACKGROUND COLOR
        switch (turn) {
            case 0:
                root.setStyle("-fx-background-image: url('image/bg_blue.jpg'); -fx-background-size: cover;");
                break;
            case 1:
                root.setStyle("-fx-background-image: url('image/bg-red.jpg'); -fx-background-size: cover;");
                break;
            default:
                root.setStyle("-fx-background-color: rgba(211, 211, 211, 0.5);");
                break;
        }

        //DRAW GRID
        for (int row = 0; row < grid.getGrid().length; row++) {
            for (int col = 0; col < grid.getGrid()[row].length; col++) {
                Button cardButton = (Button) gameGrid.getChildren().get(row * grid.getGrid().length + col);

                Label cardLabel = new Label(grid.getCard(row, col).getWord());
                Image image;



                

                if (((((game.isTurnBegin() == 0 || game.isTurnBegin() == 1) && !isMulti)  || (isMulti && isSpy)) && !grid.getCard(row, col).isSelected()) ) {
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
                } else if (grid.getCard(row, col).isSelected()) {
                    cardLabel.setText("");
                    image = switch (grid.getCard(row, col).getCouleur()) {
                        case 0 -> whiteBack;
                        case 1 -> blueBack;
                        case 2 -> redBack;
                        case 3 -> blackBack;
                        default -> null;
                    };
                } else {
                    cardLabel.setText(grid.getCard(row, col).getWord());
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

        //Draw UI

        //In multi, check if its the good team to play, if not : disable all button
        if( (context.getServer() != null && context.getServer().getServerUser().getTeamId() != game.getTurn())
        || (context.getClient() != null && context.getClient().getUser().getTeamId() != game.getTurn())){
            btnNextTurn.setVisible(false);
            btnGuess.setVisible(false);
            hintField.setText("");
            countField.setText("");
            hintField.setVisible(false);
            countField.setVisible(false);
            if((context.getServer() != null && context.getServer().getServerUser().getTeamId() != game.getTurn())
            || (context.getClient() != null && context.getClient().getUser().getTeamId() != game.getTurn())){
                whoPlays.setText("Waiting" + ((game.getTurn() != 0) ? " Blue " : " Red ") + "team to play");
            }
            labelHint.setText("");
        }else{

            
            if (game.isTurnBegin()==2 || (isMulti && !isSpy)){
                drawAgentUI();
                btnNextTurn.setVisible(true);
                btnGuess.setVisible(false);
                hintField.setText("");
                countField.setText("");
                hintField.setVisible(false);
                countField.setVisible(false);
            }
            else{
                if(!isMulti || isSpy){
                    
                    btnNextTurn.setVisible(false);
                    btnGuess.setVisible(true);
                    hintField.setVisible(true);
                    countField.setVisible(true);
                    drawSpyUI();
                }
            }
            
        }
        //check if game is over
        if (game.getIsWin()!=-1){
            EndGameDialog.showEndGameDialog(game);
        }
    }

    public void nextTurn() {
        int currentTurn = game.getTurn();
        game.setTurn((currentTurn + 1) % 2);
        game.setTurnBegin(0);
        game.notifierObservateurs();
    }

    public TimerController getTimerController(){
        return timerController;
    }

    // Resets the timer based on the configured time per turn
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
                nextTurn();
            } else if (turnState == 2) { // Agents' turn
                nextTurn();
            }
            resetTimer();
        });
    }

    public void updateRedTeamVBox() {
        imageViewRedInfo.setImage(redBack);
        imageViewRedInfo.setVisible(true);
        imageViewRedInfo.setFitWidth(100);
        imageViewRedInfo.setFitHeight(50);
        imageViewRedInfo.setPreserveRatio(false);
        imageViewRedInfo.setSmooth(true);
        String spyName = "";
        List<String> redAgentNames = new ArrayList<>();
        for (Player p: game.getRedTeam().getPlayers()){
            if (p.getIsSpy()){
                spyName = p.getName();
            }
            else {
                redAgentNames.add(p.getName());
            }
        }
        RedSpy.setFont(Font.font(customFont.getName(), customFont.getSize() + 4));
        RedAgent.setFont(Font.font(customFont.getName(), customFont.getSize() + 4));

        RedSpyName.setFont(customFont);
        RedSpyName.setTextFill(Color.WHITE);

        nbMotRedRestant.setFont(customFont);
        nbMotRedRestant.setTextFill(Color.WHITE);
        nbMotRedRestant.setText(game.getRedRemaining() + "");

        RedSpyName.setText(spyName);
        RedAgentName.getChildren().clear();
        for (String agentName : redAgentNames) {
            Label agentLabel = new Label(agentName);
            agentLabel.setFont(customFont);
            agentLabel.setTextFill(Color.WHITE);
            RedAgentName.getChildren().add(agentLabel);
        }
    }
    public void updateBlueTeamVBox() {
        imageViewBlueInfo.setImage(blueBack);
        imageViewBlueInfo.setVisible(true);
        imageViewBlueInfo.setFitWidth(100);
        imageViewBlueInfo.setFitHeight(50);
        imageViewBlueInfo.setPreserveRatio(false);
        imageViewBlueInfo.setSmooth(true);

        String spyName = "";
        List<String> blueAgentNames = new ArrayList<>();

        for (Player p : game.getBlueTeam().getPlayers()) {
            if (p.getIsSpy()) {
                spyName = p.getName();
            } else {
                blueAgentNames.add(p.getName());
            }
        }

        BlueSpy.setFont(Font.font(customFont.getName(), customFont.getSize() + 8));
        BlueAgent.setFont(Font.font(customFont.getName(), customFont.getSize() + 8));

        BlueSpyName.setFont(customFont);
        BlueSpyName.setTextFill(Color.WHITE);

        nbMotBlueRestant.setFont(customFont);
        nbMotBlueRestant.setTextFill(Color.WHITE);
        nbMotBlueRestant.setText(game.getBlueRemaining() + "");

        BlueSpyName.setText(spyName);

        BlueAgentName.getChildren().clear();
        for (String agentName : blueAgentNames) {
            Label agentLabel = new Label(agentName);
            agentLabel.setFont(customFont);
            agentLabel.setTextFill(Color.WHITE);
            BlueAgentName.getChildren().add(agentLabel);
        }
    }

    public void drawAgentUI(){

        String message;
        ArrayList<Player> players;
        if (game.getTurn() == 0){
            players = game.getBlueTeam().getPlayers();
        }
        else{
            players = game.getRedTeam().getPlayers();
        }
        players.removeIf(Player::getIsSpy);
        if (players.size() == 1){
            message = "A toi de deviner : " + players.getFirst().getName() + ".";
        }
        else{
            message = "A vous de deviner : ";
            for (Player player : players){
                message += player.getName() +", ";
            }
            message = message.substring(0, message.length() - 2) + ".";
        }
        whoPlays.setText(message);
        whoPlays.setFont(Font.font(customFont.getName(), customFont.getSize() + 20));
        whoPlays.setTextFill(Color.WHITE);

        if(game.getCurrentHint() == null){
            whoPlays.setText("En attente de l'espion");
            message = "";
        }else{
            message = "Votre espion vous a donné comme indice : " + game.hintToString();
        }

        if((context.getServer() != null && context.getServer().getServerUser().getPlayer().getIsSpy())
        || (context.getClient() != null && context.getClient().getUser().getPlayer().getIsSpy())){
            whoPlays.setText("En attentes des agents");
            message = "";
        }
        labelHint.setText(message);
        labelHint.setFont(Font.font(customFont.getName(), customFont.getSize() + 20));
        labelHint.setTextFill(Color.WHITE);
        btnNextTurn.setVisible(true);
    }

    public void drawSpyUI(){

        String message ="";
        Player spy = null;
        if (game.getTurn() == 0){
            for(Player p: game.getBlueTeam().getPlayers()){
                if (p.getIsSpy()){
                    spy = p;
                    break;
                }
            }
        }
        else{
            for(Player p: game.getRedTeam().getPlayers()){
                if (p.getIsSpy()){
                    spy = p;
                    break;
                }
            }
        }
        message = "Choisis un indice pour tes Agents, ";
        if (spy != null){
            message += spy.getName() + ".";
        }
        whoPlays.setText(message);
        whoPlays.setFont(Font.font(customFont.getName(), customFont.getSize() + 20));
        whoPlays.setTextFill(Color.WHITE);
        if((context.getServer() != null && context.getServer().getServerUser().getTeamId() != game.getTurn())
        || (context.getClient() != null && context.getClient().getUser().getTeamId() != game.getTurn())){
            whoPlays.setText("Waiting" + ((game.getTurn() != 0) ? " Blue " : " Red ") + "team to play");
        }
        labelHint.setText("");
    }
}
