package linguacrypt.model;

import java.io.Serializable;

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
    private String theme;                                   // Themes the players want to use
    private int nbPlayer;                                   // Total number of player
    private int timeTurn;                                   // Maximum chosen time allowed to play
    private int maxTimeTurn = 300;                          // Maximum time allowed to play

    private GameConfiguration() {
        difficultyLevel = 1;
        maxTeamMember = 4;
        gridSize = 5;
        theme = "";
        nbPlayer = 8;
        timeTurn = -1; // Default value : infinite time
    }

    public static GameConfiguration getInstance() {
        if (configuration == null) {
            if (firstThread) {
                firstThread = false;
                Thread.currentThread();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            configuration = new GameConfiguration();
        }
        return configuration;
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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
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

    /**
    // POUR TESTS UNIQUEMENT (avec utilisation de MOCK)
    public static void setInstance(GameConfiguration configuration) {
        GameConfiguration.configuration = configuration;
    }
    */
}

