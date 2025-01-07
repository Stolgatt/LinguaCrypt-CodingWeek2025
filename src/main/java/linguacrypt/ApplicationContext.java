package linguacrypt;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import linguacrypt.controller.EditTeamController;
import linguacrypt.controller.GameController;
import linguacrypt.controller.MainMenuController;
import linguacrypt.controller.ProfileMenuController;
import linguacrypt.model.Game;
import linguacrypt.view.EditTeamView;
import linguacrypt.view.GameView;
import linguacrypt.view.MainMenuView;
import linguacrypt.view.ProfileMenuView;

public class ApplicationContext {

    //region Attributs

    /** Instance unique de ApplicationContext (Singleton). */
    private static ApplicationContext instance;

    /** Stage principal */
    private Stage primaryStage;

    /** Root layout principal de l'application. */
    private BorderPane root;
    private Node editTeamNode;
    private Node MainMenuNode;
    private Node GameNode;
    private Node ProfileMenuNode;
    /** Références aux contrôleurs. */
    private MainMenuController mainMenuController;
    private EditTeamController editTeamController;
    private GameController gameController;
    private ProfileMenuController profileMenuController;

    /** Vues. */
    private MainMenuView mainMenuView;
    private EditTeamView editTeamView;
    private GameView gameView;
    private ProfileMenuView profileMenuView;

    /** Modèles */
    private Game game;

    //endregion

    //region Constructeur

    /**
     * Constructeur privé pour empêcher l'instanciation externe.
     */
    private ApplicationContext() {}

    //endregion

    //region Méthodes

    /**
     * Fournit l'instance unique de ApplicationContext.
     *
     * @return Instance de ApplicationContext.
     */
    public static synchronized ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    /**
     * Obtient le root layout principal.
     *
     * @return Le BorderPane root.
     */
    public BorderPane getRoot() {
        return root;
    }

    /**
     * Définit le root layout principal.
     *
     * @param root Le BorderPane root à définir.
     */
    public void setRoot(BorderPane root) {
        this.root = root;
    }

    /**
     * Initialisation des composants de l'application.
     */
    public void initialize() {
        try {

        //Load main menu components
        FXMLLoader mainMenuLoad = new FXMLLoader(getClass().getResource("/FXML/MainMenu.fxml"));
        MainMenuNode =mainMenuLoad.load();
        mainMenuView = mainMenuLoad.getController();
        mainMenuController = new MainMenuController();
        mainMenuController.setView(mainMenuView);
        //Load edit team scene components
        FXMLLoader editTeamLoader = new FXMLLoader(getClass().getResource("/FXML/editTeam.fxml"));
        editTeamNode = editTeamLoader.load();
        editTeamView = editTeamLoader.getController();
        editTeamController = new EditTeamController(game, editTeamView);
        //Load GameView components
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/FXML/Game.fxml"));
        GameNode = gameLoader.load();
        gameView = gameLoader.getController();
        gameController = new GameController(game, gameView);

        FXMLLoader profileMenuLoader = new FXMLLoader(getClass().getResource("/FXML/ProfileMenu.fxml"));
        ProfileMenuNode = profileMenuLoader.load();
        profileMenuView = profileMenuLoader.getController();
        profileMenuController = new ProfileMenuController(game, profileMenuView);
            
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des composants de l'application : " + e.getMessage());
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    //region Getters et Setters

    public Stage getPrimaryStage() {return primaryStage;}

    public void setPrimaryStage(Stage primaryStage) {this.primaryStage = primaryStage;}

    public void setGame(Game game){
        this.game = game; 
        editTeamView.setGame(game);
        gameView.setGame(game);
        gameView.setTimer();
        gameController.setGame(game);
        editTeamController.setGame(game);
        profileMenuController.setGame(game);
    }

    public Node getEditTeamNode(){
        return editTeamNode;
    }
    //endregion

    public Node getMainMenuNode() {
        return MainMenuNode;
    }

    public Node getGameNode() {
        return GameNode;
    }

    public Node getProfileMenuNode() {return ProfileMenuNode;
    }

    //endregion

    
}
