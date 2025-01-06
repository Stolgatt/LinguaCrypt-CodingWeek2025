package linguacrypt.model;

import linguacrypt.view.Observer;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private GameConfiguration gConfig;
    private Grid grid;
    private int turn; // 0 : Blue team to play / 1 : Red team to play
    private int turnStep = 0;
    private String currentHint;
    private int currentNumberWord;
    private transient ArrayList<Observer> obs = new ArrayList<>();
    private int currentTryCount = 0;
    private int isWin = -1;
    private transient Random random = new Random();

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
    public void ajouterObservateur(Observer o) {
        this.obs.add(o) ;
    }
    public void notifierObservateurs() {
        if (obs == null) {
            obs = new ArrayList<>();
        }
        for (Observer o : obs) {
            o.reagir();
        }
    }
    public int isTurnBegin() {return turnStep;}
    public void setTurnBegin(int turnBegin) {this.turnStep = turnBegin;}
    public int getCurrentTryCount() {return currentTryCount;}
    public void setCurrentTryCount(int currentTryCount) {this.currentTryCount = currentTryCount;}
    public void setIsWin(int isWin) {this.isWin = isWin;}
    public int getIsWin(){return isWin;}
    public void setGrid(Grid loadedGrid) {
        if (this.grid == null) {
            this.grid = new Grid(loadedGrid.getGrid().length);
        }
        this.grid.copyFrom(loadedGrid);
    }

    //Init Game
    public void setUpGame(){
        turn = random.nextInt(2);
        grid.initGrid(turn);
    }

    //Set a card visible
    public void flipCard(int row, int col) {
        grid.getCard(row,col).flipCard();
    }

    public void increaseTryCounter(){
        currentTryCount++;
    }

    public int isWinning(){
        boolean blueWinner = true;
        boolean redWinner = true;
        for (int row = 0; row < grid.getGrid().length; row++) {
            for (int col = 0; col < grid.getGrid()[row].length; col++) {
                Card currentCard = grid.getCard(row, col);
                if (!currentCard.isSelected()){
                    if (currentCard.getCouleur() == 1){
                        blueWinner = false;
                    }
                    else if (currentCard.getCouleur() == 2){
                        redWinner = false;
                    }
                }
            }
        }
        if (blueWinner){
            isWin = 0;
        }
        else if (redWinner){
            isWin = 1;
        }
        return isWin;
    }

    public String hintToString(){
        if (turnStep == 2){
            return currentHint + " en " + currentNumberWord;
        }
        else{
            return "";
        }
    }

    // Add these methods to manage observers
    public ArrayList<Observer> getObservateurs() {
        return new ArrayList<>(obs);
    }

    public void clearObservateurs() {
        obs.clear();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        obs = new ArrayList<>();  // Reinitialize the observers list
    }

    
}
