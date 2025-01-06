package linguacrypt.model;

import java.util.Random;

public class Game {
    private GameConfiguration gConfig;
    private Grid grid;
    private int turn; // 0 : Blue team to play / 1 : Red team to play
    private String currentHint;
    private int currentNumberWord;
    Random random = new Random();

    public Game(GameConfiguration gConfig) {
        this.gConfig = gConfig;
        this.grid = new Grid(gConfig.getGridSize());
        setUpGame();
        grid.printGrid();
    }

    //SETTER AND GETTER
    public GameConfiguration getgConfig() {return gConfig;}
    public Grid getGrid() {return grid;}
    public int getTurn() {return turn;}
    public void setTurn(int turn) {this.turn = turn;}
    public String getCurrentHint() {return currentHint;}
    public void setCurrentHint(String currentHint) {this.currentHint = currentHint;}
    public int getCurrentNumberWord() {return currentNumberWord;}
    public void setCurrentNumberWord(int currentNumberWord) {this.currentNumberWord = currentNumberWord;}

    //Init Game
    public void setUpGame(){
        turn = random.nextInt(2);
        grid.initGrid(turn);
    }

    //Set a card visible
    public void flipCard(int row, int col) {
        grid.getCard(row,col).flipCard();
    }


}
