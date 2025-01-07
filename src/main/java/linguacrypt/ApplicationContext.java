package linguacrypt;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import linguacrypt.controller.MainMenuController;
import linguacrypt.view.MainMenuView;

public class ApplicationContext {

    //region Attributs

    /** Instance unique de ApplicationContext (Singleton). */
    private static ApplicationContext instance;

    /** Stage principal */
    private Stage primaryStage;

    /** Root layout principal de l'application. */
    private BorderPane root;

    /** Références aux contrôleurs. */
    private MainMenuController mainMenuController;

    /** Modèles et vues. */
    private MainMenuView mainMenuView;

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

        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/FXML/mainMenuView.fxml"));
        root =menuLoader.load();
        MainMenuView menuView = menuLoader.getController();
        MainMenuController mainMenuController = new MainMenuController();
        mainMenuController.setView(menuView);

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des composants de l'application : " + e.getMessage());
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    //region Getters et Setters

    public Stage getPrimaryStage() {return primaryStage;}

    public void setPrimaryStage(Stage primaryStage) {this.primaryStage = primaryStage;}


    //endregion

    //endregion

    
}
