package linguacrypt.view.DialogBox;

import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.players.Team;


import java.io.IOException;

public class EndGameDialog {
    static ApplicationContext context = ApplicationContext.getInstance();
    public static void showEndGameDialog(Game game) {
        // Créez une boîte de dialogue
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Résumé de la partie");

        // Configuration du style
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: #f0f8ff; -fx-padding: 20; -fx-border-color: black; -fx-border-radius: 10; -fx-background-radius: 10;");

        // Résultat de la partie
        String resultMessage;
        switch (game.getIsWin()) {
            case 0:
                resultMessage = "L'équipe bleue a gagné ! 🎉";
                break;
            case 1:
                resultMessage = "L'équipe rouge a gagné ! 🎉";
                break;
            case 2:
                resultMessage = "L'équipe qui jouait a trouvé le mot noir... 😢";
                break;
            default:
                resultMessage = "La partie est toujours en cours.";
                break;
        }

        // Durée de la partie
        long durationMillis = System.currentTimeMillis() - game.getStartTime();
        long minutes = (durationMillis / 1000) / 60;
        long seconds = (durationMillis / 1000) % 60;
        String duration = String.format("%02d:%02d", minutes, seconds);

        // Informations sur les équipes
        Team blueTeam = game.getBlueTeam();
        Team redTeam = game.getRedTeam();
        String blueTeamPlayers = String.join(", ", blueTeam.getPlayersNames());
        String redTeamPlayers = String.join(", ", redTeam.getPlayersNames());

        // Ajout des éléments dans la grille
        grid.add(new Label("Résultat :"), 0, 0);
        grid.add(new Label(resultMessage), 1, 0);

        grid.add(new Label("Nombre de tours :"), 0, 1);
        grid.add(new Label(String.valueOf(game.getNbTour())), 1, 1);

        grid.add(new Label("Durée de la partie :"), 0, 2);
        grid.add(new Label(duration), 1, 2);

        grid.add(new Label("Équipe bleue :"), 0, 3);
        grid.add(new Label(blueTeamPlayers), 1, 3);

        if (game.getgConfig().getGameMode()!=2){
            grid.add(new Label("Équipe rouge :"), 0, 4);
            grid.add(new Label(redTeamPlayers), 1, 4);
        }

        // Ajout du style
        grid.getChildren().forEach(node -> {
            if (node instanceof Label) {
                ((Label) node).setStyle("-fx-font-size: 14px;");
            }
        });

        // Configurez la boîte de dialogue
        dialog.getDialogPane().setContent(grid);
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                // Exécuter l'action lorsque "OK" est cliqué
                context.getRoot().setCenter(context.getMainMenuNode());
                game.setIsWin(-1);
                try {
                    game.updateStat();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });
        // Afficher la boîte de dialogue
        dialog.showAndWait();
    }
}