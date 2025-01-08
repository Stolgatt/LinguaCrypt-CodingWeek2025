package linguacrypt.utils;

import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;

import java.io.*;

public class GameUtils {
    private static final String SAVE_FILE_PATH = "game_save.dat";

    public static void saveGame(Game game) throws IOException {
        var observers = game.getObservateurs();
        game.clearObservateurs();

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(SAVE_FILE_PATH))) {
            // Debug print
            System.out.println("Saving game with grid content:");
            for (int i = 0; i < game.getGrid().getGrid().length; i++) {
                for (int j = 0; j < game.getGrid().getGrid()[i].length; j++) {
                    System.out.print(game.getGrid().getCard(i, j).getWord() + " ");
                }
                System.out.println();
            }
            
            oos.writeObject(game);
        } finally {
            observers.forEach(game::ajouterObservateur);
        }
    }

    public static Game loadGame() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(SAVE_FILE_PATH))) {
            Game loadedGame = (Game) ois.readObject();
            
            // Override GameConfiguration fields with loaded data
            GameConfiguration config = GameConfiguration.getInstance();
            GameConfiguration loadedConfig = loadedGame.getGameConfiguration();
            
            config.setDifficultyLevel(loadedConfig.getDifficultyLevel());
            config.setMaxTeamMember(loadedConfig.getMaxTeamMember());
            config.setGridSize(loadedConfig.getGridSize());
            switch (loadedConfig.getGameMode()) {
                case 0:
                    config.setWordTheme(loadedConfig.getWordTheme());
                    break;
                case 1:
                    config.setPictTheme(loadedConfig.getPictTheme());
                    break;
                default:
                    break;
            }
            config.setNbPlayer(loadedConfig.getNbPlayer());
            config.setTimeTurn(loadedConfig.getTimeTurn());
            
            // Debug print
            System.out.println("Loaded game with grid content:");
            for (int i = 0; i < loadedGame.getGrid().getGrid().length; i++) {
                for (int j = 0; j < loadedGame.getGrid().getGrid()[i].length; j++) {
                    System.out.print(loadedGame.getGrid().getCard(i, j).getWord() + " ");
                }
                System.out.println();
            }
            
            return loadedGame;
        }
    }
}