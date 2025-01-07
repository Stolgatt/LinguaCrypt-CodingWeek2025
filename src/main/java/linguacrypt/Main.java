package linguacrypt;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import linguacrypt.model.GameConfiguration;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
        GameConfiguration config = GameConfiguration.getInstance();
        config.loadPlayerList();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Main.fxml"));
        BorderPane root = loader.load();

        ApplicationContext applicationContext = ApplicationContext.getInstance();
        applicationContext.initialize();
        applicationContext.setRoot(root);
        applicationContext.setPrimaryStage(primaryStage);
        root.setCenter(applicationContext.getMainMenuNode());
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("LinguaCrypt");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
