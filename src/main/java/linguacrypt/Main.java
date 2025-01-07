package linguacrypt;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        ApplicationContext applicationContext = ApplicationContext.getInstance();
        applicationContext.initialize();
        applicationContext.setPrimaryStage(primaryStage);

        Scene scene = new Scene(applicationContext.getRoot(), 800, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("LinguaCrypt");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
