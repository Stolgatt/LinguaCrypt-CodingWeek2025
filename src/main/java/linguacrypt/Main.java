package linguacrypt;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import linguacrypt.controller.MainMenuController;
import linguacrypt.view.MainMenuView;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/FXML/mainMenuView.fxml"));
        Parent root =menuLoader.load();
        MainMenuView menuView = menuLoader.getController();
        MainMenuController mainMenuController = new MainMenuController();
        mainMenuController.setView(menuView);

        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("LinguaCrypt");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
