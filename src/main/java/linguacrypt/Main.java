package linguacrypt;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL fxmlURL = getClass().getResource("/FXML/main.fxml");
        if (fxmlURL == null) {
            System.err.println("Could not find main.fxml");
            System.exit(1);
        }
        Parent root = FXMLLoader.load(fxmlURL);

        Scene scene = new Scene(root, 1000, 1000);
        primaryStage.setScene(scene);
        primaryStage.setTitle("LinguaCrypt");
        primaryStage.show();
    }

    public static void main(String[] args) {
        GameConfiguration gConfig = GameConfiguration.getInstance();
        Game game = new Game(gConfig);


        launch();
    }

}
