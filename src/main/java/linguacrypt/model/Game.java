package linguacrypt.model;

import java.util.Random;

public class Game {
    private GameConfiguration gConfig;
    private Grid grid;
    private int turn; // 0 : Blue team to play / 1 : Red team to play
    Random random = new Random();

    public Game(GameConfiguration gConfig, Grid grid) {
        this.gConfig = gConfig;
        this.grid = grid;
    }

    public void setUpGame(){
        turn = random.nextInt(2);
    }
}
