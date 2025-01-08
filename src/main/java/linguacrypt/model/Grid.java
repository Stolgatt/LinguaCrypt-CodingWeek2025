package linguacrypt.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Grid implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int size;
    private Card[][] grid;
    private transient Random random = new Random();

    public ArrayList<String> getGridWords() {
        return gridWords;
    }

    public void setGridWords(ArrayList<String> gridWords) {
        this.gridWords = gridWords;
    }

    private ArrayList<String> gridWords;
    private List<String> themeWords;
    private int gameMode;

    public Grid(int size, List<String> themeWords, int gameMode) {
        this.size = size;
        this.grid = new Card[size][size];
        this.gridWords = new ArrayList<>();
        this.gameMode = gameMode;
        this.themeWords = themeWords;
    }

    public void initGrid(int turn) {
        gridWords = new ArrayList<>();
        int white = 0;
        int blue = 0;
        int red = 0;
        int black = 0;

        int nbCard = size * size;
        int maxBlue;
        int maxRed;
        if (turn!=-2){
            maxRed= nbCard / 3 + turn;
            maxBlue = nbCard / 3 + 1 - turn;
        }
        else{
            maxRed= 0;
            maxBlue = nbCard / 3 + 1;
        }
        int maxWhite = nbCard - maxBlue - maxRed - 1;

        System.out.println(nbCard + " " + maxBlue + " " + maxRed + " " + maxWhite + " turn: " + turn);

        if ((gameMode == 0 || gameMode==2) && (themeWords == null || themeWords.isEmpty())) {
            throw new IllegalArgumentException("Theme words list is empty. Cannot initialize grid.");
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String randomWord = null;
                String randomImage = null;
                if (gameMode == 0 || gameMode == 2) {
                    randomWord = themeWords.get(random.nextInt(themeWords.size()));
                    while (gridWords.contains(randomWord)) {
                        randomWord = themeWords.get(random.nextInt(themeWords.size()));
                    }
                    gridWords.add(randomWord);
                } else if (gameMode == 1) {
                    randomImage = themeWords.get(random.nextInt(themeWords.size()));
                    while (gridWords.contains(randomImage)){
                        randomImage = themeWords.get(random.nextInt(themeWords.size()));
                    }
                    gridWords.add(randomImage);
                }
                grid[i][j] = new Card(randomWord, randomImage);
                int color;
                while (true) {
                    color = random.nextInt(4);
                    if (color == 3 && black == 0) { black++; break; }
                    if (color == 2 && red < maxRed) { red++; break; }
                    if (color == 1 && blue < maxBlue) { blue++; break; }
                    if (color == 0 && white < maxWhite) { white++; break; }
                }
                grid[i][j].setCouleur(color);
            }
        }
    }

    public Card[][] getGrid(){
        return grid;
    }

    public Card getCard(int row, int col) {
        if (row >= 0 && row < size && col >= 0 && col < size) {
            return grid[row][col];
        }
        return null;
    }

    public void setCard(int row, int col, Card card) {
        if (row >= 0 && row < size && col >= 0 && col < size) {
            grid[row][col] = card;
        }
    }

    public void printGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (gameMode == 0 || gameMode == 2) {
                    System.out.print(grid[i][j].getWord() + " (" + grid[i][j].getCouleur() + ")" );
                } else {
                    System.out.print(grid[i][j].getUrlImage() + " (" + grid[i][j].getCouleur() + ")" );
                }
            }
            System.out.println();
        }
    }

    public void setGrid(Card[][] grid) {
        this.grid = grid;
    }

    public void copyFrom(Grid other) {
        this.size = other.size;
        this.grid = new Card[size][size];
        this.gridWords = new ArrayList<>(other.gridWords);
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Card originalCard = other.getCard(i, j);
                this.grid[i][j] = new Card(
                    originalCard.getWord(),
                    originalCard.getUrlImage()
                );
                this.grid[i][j].setCouleur(originalCard.getCouleur());
                this.grid[i][j].setSelected(originalCard.isSelected());
            }
        }
    }

    public List<String> getRemainingWordsForTeam(int teamID){
        List<String> remainingWords = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!grid[i][j].isSelected() && teamID + 1 ==grid[i][j].getCouleur()) {
                    remainingWords.add(grid[i][j].getWord());
                }
            }
        }
        return remainingWords;
    }
    public List<String> getRemainingWords(){
        List<String> remainingWords = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!grid[i][j].isSelected()){
                    remainingWords.add(grid[i][j].getWord());
                }
            }
        }
        return remainingWords;
    }

}
