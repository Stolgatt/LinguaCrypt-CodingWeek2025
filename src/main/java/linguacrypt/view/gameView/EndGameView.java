package linguacrypt.view.gameView;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;

import java.io.IOException;

public class EndGameView {
    @FXML
    private Label resultTitle, duration, nbTour,motRestant;

    private Game game;
    private GameConfiguration gConfig = GameConfiguration.getInstance();
    private ApplicationContext context = ApplicationContext.getInstance();

    public void setGame(Game game) {
        this.game = game;
    }
    @FXML
    private void goBackToMainMenu() {
        context.getRoot().setCenter(context.getMainMenuNode());
    }

    public void showGameResult() {
        setBackgroundColor();
        setResultTitle();
        setStat();
        try {
            game.updateStat();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setBackgroundColor() {
        Node root = context.getRoot();
        if (game.getIsWin() == 2) {
            int turn = game.getTurn();
            switch (turn) {
                case 0:
                    root.setStyle("-fx-background-image: url('image/bg_blue.jpg'); -fx-background-size: cover;");
                    break;
                case 1:
                    root.setStyle("-fx-background-image: url('image/bg-red.jpg'); -fx-background-size: cover;");
                    break;
            }
        }
        else{
            switch (game.getIsWin()) {
                case 0:
                    root.setStyle("-fx-background-image: url('image/bg_blue.jpg'); -fx-background-size: cover;");
                    break;
                case 1:
                    root.setStyle("-fx-background-image: url('image/bg-red.jpg'); -fx-background-size: cover;");
                    break;
            }
        }
    }

    private void setResultTitle() {
        String resultMessage;
        switch (game.getIsWin()) {
            case 0:
                resultMessage = "L'équipe bleue a gagné ! 🎉";
                break;
            case 1:
                resultMessage = "L'équipe rouge a gagné ! 🎉";
                break;
            case 2:
                resultMessage = "L'équipe ";
                if (game.getTurn() == 0){
                    resultMessage += "Bleue";
                }
                else{
                    resultMessage += "Rouge";
                }
                resultMessage += " a trouvé l'Assassin... 😢";
                break;
            default:
                resultMessage = "La partie est toujours en cours.";
                break;
        }
        resultTitle.setText(resultMessage);
    }
    private void setStat() {
        nbTour.setText(String.valueOf(game.getNbTour()));

        long durationMillis = System.currentTimeMillis() - game.getStartTime();
        long minutes = (durationMillis / 1000) / 60;
        long seconds = (durationMillis / 1000) % 60;
        String duree = String.format("%02d:%02d", minutes, seconds);
        duration.setText(duree);

        int rougeRestant = game.getRedRemaining();
        int bleuRestant = game.getBlueRemaining();
        motRestant.setText(String.valueOf(rougeRestant + bleuRestant));
    }
}
