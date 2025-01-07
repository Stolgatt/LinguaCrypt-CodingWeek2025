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
    private ArrayList<String> gridWords;
    private List<String> themeWords;
    private int gameMode;

    public Grid(int size, List<String> themeWords, int gameMode) {
        this.size = size;
        this.grid = new Card[size][size];
        this.gridWords = new ArrayList<>();
        this.gameMode = gameMode;
        if (gameMode == 0) {
            this.themeWords = themeWords;
        } else {
            this.themeWords = null;
        }
    }

    public void initGrid(int turn) {
        gridWords = new ArrayList<>();
        int white = 0;
        int blue = 0;
        int red = 0;
        int black = 0;

        int nbCard = size * size;
        int maxBlue = nbCard / 3 + 1 - turn;
        int maxRed = nbCard / 3 + turn;
        int maxWhite = nbCard - maxBlue - maxRed - 1;

        System.out.println(nbCard + " " + maxBlue + " " + maxRed + " " + maxWhite + " turn: " + turn);

        if (gameMode == 0 && (themeWords == null || themeWords.isEmpty())) {
            throw new IllegalArgumentException("Theme words list is empty. Cannot initialize grid.");
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String randomWord = null;
                String randomImage = null;
                if (gameMode == 0) {
                    randomWord = themeWords.get(random.nextInt(themeWords.size()));
                    while (gridWords.contains(randomWord)) {
                        randomWord = themeWords.get(random.nextInt(themeWords.size()));
                    }
                    gridWords.add(randomWord);
                } else if (gameMode == 1) {
                    List<String> themePictures = loadPicturesFromFile("pictures/picturesPath.txt");
                    randomImage = themePictures.get(random.nextInt(themePictures.size()));
                    while (gridWords.contains(randomImage)){
                        randomImage = themePictures.get(random.nextInt(themePictures.size()));
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
                if (gameMode == 0) {
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

    private List<String> loadPicturesFromFile(String filePath) {
        List<String> pictures = new ArrayList<>();   
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new FileNotFoundException(filePath + " not found!");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                pictures.add(line);
            }
            reader.close();
        } catch(IOException e) {
            e.printStackTrace();
            System.err.println(filePath + " not found!");
        }
        return pictures;
    }
}
