package linguacrypt.view.gameView;

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


    private TimerController timerController;

    private Dialog<Void> spyDialog;

    private Game game;
    private ApplicationContext context = ApplicationContext.getInstance();
    private Runnable onNextTurn;
    private Runnable OnGiveHint;
    private BiConsumer<Integer, Integer> onCardClicked;

    Image redBack = new Image(getClass().getResourceAsStream("/image/backs_part_4.png"));
    Image blueBack = new Image(getClass().getResourceAsStream("/image/backs_part_1.png"));

    @FXML
    private void initialize() {
        btnNextTurn.setOnAction(e -> {
            btnNextTurn.setVisible(false);
            if (onNextTurn != null) onNextTurn.run();

            resetTimer();
        });
        btnGuess.setOnAction(e -> {
            btnGuess.setVisible(false);
            if (OnGiveHint != null) OnGiveHint.run();
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

        initializeGrid();
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
                imageView.setFitWidth(100);  // Largeur fixe
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
    public void setOnGiveHint(Runnable OnGiveHint) {
        this.OnGiveHint = OnGiveHint;
    }
    public void setonCardClicked(BiConsumer<Integer, Integer> onCardClicked) {
        this.onCardClicked = onCardClicked;
    }

    public void reagir() {
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

                if ((game.isTurnBegin() == 0 || game.isTurnBegin() == 1) && !grid.getCard(row, col).isSelected()) {
                    cardLabel.setText(grid.getCard(row, col).getWord());
                    if (grid.getCard(row, col).getCouleur() == 3){
                        cardLabel.setTextFill(Color.WHITE);
                    }
                    image = switch (grid.getCard(row, col).getCouleur()) {
                        case 0 -> new Image(getClass().getResourceAsStream("/image/front_white.png"));
                        case 1 -> new Image(getClass().getResourceAsStream("/image/front_blue.png"));
                        case 2 -> new Image(getClass().getResourceAsStream("/image/front_red.png"));
                        case 3 -> new Image(getClass().getResourceAsStream("/image/front_black.png"));
                        default -> null;
                    };
                } else if (grid.getCard(row, col).isSelected()) {
                    cardLabel.setText("");
                    image = switch (grid.getCard(row, col).getCouleur()) {
                        case 0 -> new Image(getClass().getResourceAsStream("/image/backs_part_2.png"));
                        case 1 -> new Image(getClass().getResourceAsStream("/image/backs_part_1.png"));
                        case 2 -> redBack;
                        case 3 -> new Image(getClass().getResourceAsStream("/image/black-back.png"));
                        default -> null;
                    };
                } else {
                    cardLabel.setText(grid.getCard(row, col).getWord());
                    image = new Image(getClass().getResourceAsStream("/image/front_white.png"));
                }

// Créer une ImageView pour l'image
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(false);
                imageView.setSmooth(true);

// Utiliser un StackPane pour superposer l'image et le texte
                StackPane stackPane = new StackPane();
                stackPane.setPrefSize(100, 50); // Taille du StackPane
                stackPane.getChildren().addAll(imageView, cardLabel); // Ajouter l'image et le texte dans le StackPane
                cardLabel.setTranslateY(10);
                cardLabel.setFont(customFont);
                cardButton.setGraphic(stackPane);
                cardButton.setContentDisplay(ContentDisplay.CENTER);
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
        if (game.getIsWin()!=-1){
            EndGameDialog.showEndGameDialog(game);
        }
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
        // Mise à jour de l'image pour l'équipe bleue
        imageViewBlueInfo.setImage(blueBack); // Assurez-vous que vous avez l'image blueBack correctement définie
        imageViewBlueInfo.setVisible(true);
        imageViewBlueInfo.setFitWidth(100);
        imageViewBlueInfo.setFitHeight(50);
        imageViewBlueInfo.setPreserveRatio(false);
        imageViewBlueInfo.setSmooth(true);

        // Variables pour le nom de l'espion et des agents bleus
        String spyName = "";
        List<String> blueAgentNames = new ArrayList<>();

        // Récupérer les joueurs de l'équipe bleue
        for (Player p : game.getBlueTeam().getPlayers()) {
            if (p.getIsSpy()) {
                spyName = p.getName(); // Si c'est l'espion, on le garde dans spyName
            } else {
                blueAgentNames.add(p.getName()); // Sinon, on ajoute l'agent à la liste
            }
        }

        // Mise à jour de la police de l'espion et des agents
        BlueSpy.setFont(Font.font(customFont.getName(), customFont.getSize() + 4));
        BlueAgent.setFont(Font.font(customFont.getName(), customFont.getSize() + 4));

        // Mise à jour de la police et du texte de l'espion
        BlueSpyName.setFont(customFont);
        BlueSpyName.setTextFill(Color.WHITE);

        // Mise à jour de la police et du texte des mots restants
        nbMotBlueRestant.setFont(customFont);
        nbMotBlueRestant.setTextFill(Color.WHITE);
        nbMotBlueRestant.setText(game.getBlueRemaining() + "");

        // Mise à jour du nom de l'espion
        BlueSpyName.setText(spyName);

        // Vider les enfants de BlueAgentName et ajouter les agents bleus
        BlueAgentName.getChildren().clear();
        for (String agentName : blueAgentNames) {
            Label agentLabel = new Label(agentName);
            agentLabel.setFont(customFont);
            agentLabel.setTextFill(Color.WHITE);
            BlueAgentName.getChildren().add(agentLabel);
        }
    }
}
