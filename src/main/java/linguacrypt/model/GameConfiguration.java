package linguacrypt.model;

import linguacrypt.model.statistique.GameStat;
import linguacrypt.utils.StatLoader;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class GameConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;
    private static transient GameConfiguration configuration = null;
    static transient boolean firstThread = true;
    private int difficultyLevel;                            // For the AI
    private int maxDiffLevel = 3;
    private int minDiffLevel = 1;
    private int maxTeamMember;                              // Number of players in each team
    private int gridSize;                                   // size of the grid of words (gridSize * gridSize words)
    private int maxGridSize = 15;
    private String wordTheme;                                   // Themes for the words the players want to use
    private String pictTheme;                                   // Themes for the pictures the players want to use
    private int nbPlayer;                                   // Total number of player
    private int timeTurn;                                   // Maximum chosen time allowed to play
    private int maxTimeTurn = 300;                          // Maximum time allowed to play
    private ArrayList<Player> playerList;
    private GameStat gameStat;

    private int gameMode;                                   // 0 : Normal Words, 1 : Normal Pictures, 2 : Duo, 3 : Solo


    private GameConfiguration() {
        difficultyLevel = 1;
        maxTeamMember = 4;
        gridSize = 5;
        wordTheme = "";
        pictTheme = "";
        nbPlayer = 8;
        timeTurn = -1; // Default value : infinite time
        gameMode = 0;  // Default value : Normal Words
    }

    public static GameConfiguration getInstance() {
        if (configuration == null) {
            configuration = new GameConfiguration();
        }
        return configuration;
    }

    public void loadPlayerList() throws IOException, ClassNotFoundException {
        playerList = StatLoader.loadPlayerList();
    }


    public static void resetInstance() {
        configuration = null;
        firstThread = true;
    }
    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getMaxTeamMember() {
        return maxTeamMember;
    }

    public void setMaxTeamMember(int maxTeamMember) {
        this.maxTeamMember = maxTeamMember;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public String getWordTheme() {
        return wordTheme;
    }

    public void setWordTheme(String wordTheme) {
        this.wordTheme = wordTheme;
    }

    public String getPictTheme() {
        return pictTheme;
    }

    public void setPictTheme(String pictTheme) {
        this.pictTheme = pictTheme;
    }

    public int getNbPlayer() {
        return nbPlayer;
    }

    public void setNbPlayer(int nbPlayer) {
        this.nbPlayer = nbPlayer;
    }

    public int getTimeTurn() {
        return timeTurn;
    }

    public void setTimeTurn(int timeTurn) {
        this.timeTurn = timeTurn;
    }

    public int getMaxDiffLevel() {return maxDiffLevel;}

    public int getMinDiffLevel() {
        return minDiffLevel;
    }

    public int getMaxTimeTurn(){
        return maxTimeTurn;
    }

    public int getMaxGridSize(){
        return maxGridSize;
    }

    public ArrayList<Player> getPlayerList() {return playerList;}
    public void addPlayer(Player player) { if (playerList ==null){playerList = new ArrayList<>();}playerList.add(player);}

    public int getGameMode(){
        return gameMode;
    }

    public void setGameMode(int gameMode){
        this.gameMode = gameMode;
    }
    /**
    // POUR TESTS UNIQUEMENT (avec utilisation de MOCK)
    public static void setInstance(GameConfiguration configuration) {
        GameConfiguration.configuration = configuration;
    }
    */
}

