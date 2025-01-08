package linguacrypt.controller;

import javafx.event.ActionEvent;
import linguacrypt.ApplicationContext;
import linguacrypt.model.Game;
import linguacrypt.view.ProfileMenuView;

public class ProfileMenuController {
    private ProfileMenuView view;
    private Game game;
    ApplicationContext context =ApplicationContext.getInstance();

    public ProfileMenuController(Game game, ProfileMenuView view) {
        this.view = view;
        this.game = game;
        view.setOnMenuPrincipal(this::handleMainMenu);
    }

    public void setGame(Game game) {
        this.game = game;
    }
    public void handleMainMenu(ActionEvent actionEvent) {
        context.getRoot().setCenter(context.getMainMenuNode());
    }
}
