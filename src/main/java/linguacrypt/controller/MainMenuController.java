package linguacrypt.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.GameConfigurationDialog;
import linguacrypt.view.MainMenuView;


public class MainMenuController {

    MainMenuView view;
    ApplicationContext context = ApplicationContext.getInstance();

    public void setView(MainMenuView menuView){
        this.view = menuView;
        view.setOnCreateGame(this::handleCreateGame);
    }

    public void handleCreateGame(ActionEvent event) {

            // Retrieve game configuration and personalize settings
            GameConfiguration config = GameConfiguration.getInstance();
            GameConfigurationDialog dialog = new GameConfigurationDialog();
            if (dialog.showGameConfigurationDialog()) {
                config.setTheme(dialog.getSelectedTheme());
                context.setGame(new Game(config));
                context.getRoot().setCenter(context.getGameNode());
            }
    }

    @FXML
    public void handleExit(ActionEvent event) {
        System.exit(0);
    }
}
