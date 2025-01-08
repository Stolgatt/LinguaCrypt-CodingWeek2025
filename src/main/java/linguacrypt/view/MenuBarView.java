package linguacrypt.view;

import javafx.fxml.FXML;
import linguacrypt.controller.MenuBarController;

import java.io.IOException;

public class MenuBarView {
    private MenuBarController controller;

    @FXML
    public void handleSaveGame() {
        if (controller != null) {
            controller.saveGame();
        }
    }

    @FXML
    public void handleLoadGame() {
        if (controller != null) {
            controller.loadGame();
        }
    }

    @FXML
    public void handleExit() throws IOException {
        if (controller != null) {
            controller.exit();
        }
    }

    public void setController(MenuBarController controller) {
        this.controller = controller;
    }
} 