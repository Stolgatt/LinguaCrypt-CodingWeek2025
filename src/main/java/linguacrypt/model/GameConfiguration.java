package linguacrypt.model;

public class GameConfiguration {
    private static GameConfiguration configuration = null;
    static boolean firstThread = true;
    private int difficultyLevel;
    private int maxTeamMember;
    private int gridSize;
    private String theme;
    private int nbPlayer;
    private int timeTurn;

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
}

