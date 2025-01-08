package linguacrypt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import linguacrypt.model.AI.AIAgent;
import linguacrypt.model.AI.AISpy;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.Hint;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        launch();


    }

}
