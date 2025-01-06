package linguacrypt.model;

import java.util.Random;

public class Grid {
    private int size;
    private Card[][] grid;

    public Grid(int size) {
        this.size = size;
        this.grid = new Card[size][size];
    }

    public void initGrid(int turn) {
        BankCard bankCard = new BankCard();
        Random random = new Random();
        int white = 0;
        int blue = 0;
        int red = 0;
        int black = 0;

        int nbCard = size * size;
        int maxBlue = nbCard/3 + 1 - turn;
        int maxRed = nbCard/3 + turn;
        int maxWhite = nbCard - maxBlue - maxRed - 1;

        System.out.println(nbCard + " " + maxBlue + " " + maxRed + " " + maxWhite + "turn : " + turn);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String randomWord = bankCard.getWordsDefault().get(random.nextInt(bankCard.getWordsDefault().size()));
                grid[i][j] = new Card(randomWord, "url_to_image");
                int color;
                while (true){
                    color = random.nextInt(4);
                    if (color == 3 && black == 0){black++;break;}
                    if (color == 2 && red <maxRed){red++;break;}
                    if (color == 1 && blue < maxBlue){blue++;break;}
                    if (color == 0 && white < maxWhite){white++;break;}
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
                System.out.print(grid[i][j].getWord() + " (" + grid[i][j].getCouleur() + ")" );
            }
            System.out.println();
        }
    }
}
