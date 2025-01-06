package linguacrypt.view;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import linguacrypt.controller.MenuBarController;

public class MenuBarView {
    private MenuBarController controller;

    public void setController(MenuBarController controller) {
        this.controller = controller;
    }

    @FXML
    public void handleSaveGame(ActionEvent event) {
        controller.saveGame();
    }

    @FXML
    public void handleLoadGame(ActionEvent event) {
        controller.loadGame();
    }

    @FXML
    public void handleExit(ActionEvent event) {
        controller.exit();
    }
} 