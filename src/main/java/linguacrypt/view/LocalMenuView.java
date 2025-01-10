package linguacrypt.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.game.Theme;
import linguacrypt.model.players.Player;
import linguacrypt.model.statistique.PlayerStat;
import linguacrypt.utils.ThemeLoader;
import linguacrypt.view.gameView.GameViewUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalMenuView {
    @FXML
    private VBox gameView, soloView;
    @FXML
    private TextField textFieldPlayerName;
    @FXML
    private RadioButton spyButton, agentButton;
    @FXML
    private Label labelTeamSize, labelGridSize, labelTimer,labelGridSizeSolo,labelTimerSolo,labelAIDifficultySolo;

    @FXML
    private ComboBox<String> comboBoxTheme,comboBoxThemeSolo;
    @FXML
    private ToggleGroup toggleGroupSpy;
    private int teamSize = 4;
    private int gridSize = 5;
    private int timer = -1;
    private int difficulty = 2;
    private String name = "";
    private boolean isSpy = true;

    private GameConfiguration gConfig = GameConfiguration.getInstance();
    private ApplicationContext context = ApplicationContext.getInstance();
    @FXML
    public void initialize() {
        // Initialize the labels
        //Normal
        labelTeamSize.setText(String.valueOf(teamSize));
        labelGridSize.setText(String.valueOf(gridSize));
        labelTimer.setText(String.valueOf(timer));
        //Solo
        labelGridSizeSolo.setText(String.valueOf(gridSize));
        labelTimerSolo.setText(String.valueOf(timer));
        labelAIDifficultySolo.setText(String.valueOf(difficulty));
        spyButton.setToggleGroup(toggleGroupSpy);
        agentButton.setToggleGroup(toggleGroupSpy);
        spyButton.setSelected(isSpy);

        // Load themes
        List<Theme> themes = ThemeLoader.loadThemes(0);
        for (Theme theme : themes) {
            comboBoxTheme.getItems().add(theme.getName());
        }
        comboBoxTheme.getSelectionModel().selectFirst();
        for (Theme theme : themes) {
            comboBoxThemeSolo.getItems().add(theme.getName());
        }
        comboBoxThemeSolo.getSelectionModel().selectFirst();

    }

    public void loadExistingThemes() {
        comboBoxTheme.getItems().clear();
        List<Theme> themes = ThemeLoader.loadThemes(GameConfiguration.getInstance().getGameMode());
        for (Theme theme : themes) {
            comboBoxTheme.getItems().add(theme.getName());
        }
        comboBoxTheme.getSelectionModel().selectFirst();
    }


    @FXML
    public void showGameView() {
        context.getRoot().setCenter(context.getMPMenuNode());
        gameView.setVisible(true);
        soloView.setVisible(false);
    }

    @FXML
    public void showSoloView() {
        context.getRoot().setCenter(context.getMPMenuNode());
        soloView.setVisible(true);
        gameView.setVisible(false);
    }
    @FXML
    private void decreaseTeamSize() {
        if (teamSize > 2) {
            teamSize--;
        }
        if (gConfig.getGameMode() == 0 || gConfig.getGameMode() == 1) {
            labelTeamSize.setText(String.valueOf(teamSize));
        }
    }

    @FXML
    private void increaseTeamSize() {
        if (teamSize < 10) {
            teamSize++;
            labelTeamSize.setText(String.valueOf(teamSize));
        }
    }

    @FXML
    private void decreaseGridSize() {
        if (gridSize > 3) {
            gridSize--;
            labelGridSize.setText(String.valueOf(gridSize));
        }
        if (gConfig.getGameMode() == 0 || gConfig.getGameMode() == 1){
            labelGridSize.setText(String.valueOf(gridSize));
        }else if (gConfig.getGameMode() == 2){
            labelGridSizeSolo.setText(String.valueOf(gridSize));
        }
    }

    @FXML
    private void increaseGridSize() {
        if (gridSize < 7) {
            gridSize++;
            labelGridSize.setText(String.valueOf(gridSize));
        }
        if (gConfig.getGameMode() == 0 || gConfig.getGameMode() == 1){
            labelGridSize.setText(String.valueOf(gridSize));
        }else if (gConfig.getGameMode() == 2){
            labelGridSizeSolo.setText(String.valueOf(gridSize));
        }
    }

    @FXML
    private void decreaseTimer() {
        if (timer > 10) {
            timer -= 10;
        }
        else{
            timer = -1;
        }
        if (gConfig.getGameMode() == 0|| gConfig.getGameMode() == 1) {
            labelTimer.setText(String.valueOf(timer));
        }else if (gConfig.getGameMode() == 2){
            labelTimerSolo.setText(String.valueOf(timer));
        }
    }

    @FXML
    private void increaseTimer() {
        if (timer < 0) {
            timer = 10;
        }
        else if (timer < 120) {
            timer += 10;
        }
        if (gConfig.getGameMode() == 0|| gConfig.getGameMode() == 1){
            labelTimer.setText(String.valueOf(timer));
        }else if (gConfig.getGameMode() == 2){
            labelTimerSolo.setText(String.valueOf(timer));
        }
    }
    @FXML
    private void decreaseAILevel() {
        if (difficulty > 1) {
            difficulty -= 1;
            labelAIDifficultySolo.setText(String.valueOf(difficulty));
        }
    }

    @FXML
    private void increaseAILevel() {
        if (difficulty < 3) {
            difficulty+= 1;
            labelAIDifficultySolo.setText(String.valueOf(difficulty));
        }
    }
    @FXML
    private void goBackToMainMenu() {
        gConfig.setGameMode(0);
        context.getRoot().setCenter(context.getMainMenuNode());
    }

    @FXML
    private void createNormal(){
        if (gConfig.getGameMode() == 0){
            gConfig.setWordTheme(comboBoxTheme.getSelectionModel().getSelectedItem());
        }
        else if (gConfig.getGameMode() == 1){
            gConfig.setPictTheme(comboBoxTheme.getSelectionModel().getSelectedItem());
        }
        gConfig.setGridSize(gridSize);
        gConfig.setTimeTurn(timer);
        context.setGame(new Game(gConfig));
        context.getRoot().setCenter(context.getGameNode());
    }


    @FXML
    private void createSolo(){
        name = textFieldPlayerName.getText();
        isSpy = spyButton.isSelected();
        gConfig.setDifficultyLevel(difficulty);
        gConfig.setGridSize(gridSize);
        gConfig.setWordTheme(comboBoxTheme.getSelectionModel().getSelectedItem());
        gConfig.setTimeTurn(timer);

        Game game = new Game(gConfig);
        try {
            if (name.isEmpty()) {
                GameViewUtils.showError("Vous devez entrer votre nom !");
                return;
            }
            context.setGame(tryAddPlayer(name,isSpy,game));
            context.getRoot().setCenter(context.getSoloGameNode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Game tryAddPlayer(String playerName,boolean isSpy,Game game) throws IOException, ClassNotFoundException {

        ArrayList<Player> playerList = game.getgConfig().getPlayerList();
        if (playerList == null){
            playerList = new ArrayList<>();
        }
        boolean find = false;
        for (Player p : playerList){
            if (p.getName().equals(playerName)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Profil existant");
                alert.setHeaderText("Le joueur existe déjà !");
                alert.setContentText("Un joueur portant ce nom existe déjà. Voulez-vous utiliser ce profil existant ?");

                ButtonType yesButton = new ButtonType("Oui", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(yesButton, noButton);

                alert.showAndWait().ifPresent(response -> {
                    if (response == yesButton) {
                        p.setIsSpy(isSpy);
                        game.setUpSoloGame(p);
                    }
                });
                find = true;
                break;
            }
        }
        if (!find) {
            Player newPlayer = new Player(playerName,isSpy,"",new PlayerStat());
            game.setUpSoloGame(newPlayer);
        }
        return game;
    }
}
