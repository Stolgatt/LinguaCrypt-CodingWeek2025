package linguacrypt.utils;

import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.players.Player;

import java.io.*;
import java.util.ArrayList;

public class StatLoader {
    private static final String SAVE_FILE_PATH = "playerList.dat";

    public static void saveGame(Game game) throws IOException {
        var observers = game.getObservateurs();
        game.clearObservateurs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_PATH))) {
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

    public static ArrayList<Player> loadPlayerList() throws IOException, ClassNotFoundException {
        File file = new File(SAVE_FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            ArrayList<Player> playerList = new ArrayList<>();
            while (true) {
                try {
                    Player player = (Player) ois.readObject();
                    System.out.println(player.getName() + " " + player.getStat().displayStat());
                    playerList.add(player);
                } catch (EOFException e) {
                    break;
                }
            }
            return playerList;
        }
    }

    // Ajouter un joueur en mode "append"
    public static void addPlayer(Player player) throws IOException {
        boolean append = new File(SAVE_FILE_PATH).exists();
        try (ObjectOutputStream oos = append
                ? new AppendingObjectOutputStream(new FileOutputStream(SAVE_FILE_PATH, true))
                : new ObjectOutputStream(new FileOutputStream(SAVE_FILE_PATH))) {
            oos.writeObject(player);
        }
    }
    static class AppendingObjectOutputStream extends ObjectOutputStream {
        public AppendingObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset(); // Éviter d'écrire un nouvel en-tête
        }
    }
    public static void overwritePlayerList(ArrayList<Player> playerList) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_PATH))) {
            for (Player player : playerList) {
                oos.writeObject(player);
            }
        }
        System.out.println("La liste des joueurs a été écrasée avec succès.");
    }
}
