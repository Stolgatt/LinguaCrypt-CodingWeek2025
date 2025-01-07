package linguacrypt.controller;

import linguacrypt.model.Game;
import linguacrypt.view.ProfileMenuView;

public class ProfileMenuController {
    private ProfileMenuView view;
    private Game game;
    public ProfileMenuController(Game game, ProfileMenuView view) {
        this.view = view;
        this.game = game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
