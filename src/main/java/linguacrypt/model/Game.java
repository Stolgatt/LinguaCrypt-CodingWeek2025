package linguacrypt.model;

import java.util.Random;

public class Game {
    private GameConfiguration gConfig;
    private Grid grid;
    private int turn; // 0 : Blue team to play / 1 : Red team to play
    Random random = new Random();

    public Game(GameConfiguration gConfig) {
        this.gConfig = gConfig;
        this.grid = new Grid(gConfig.getGridSize());
        setUpGame();
        grid.printGrid();
    }

    public void setUpGame(){
        turn = random.nextInt(2);
        grid.initGrid();
    }

    public GameConfiguration getgConfig() {
        return gConfig;
    }

    public Grid getGrid() {
        return grid;
    }


    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
}
