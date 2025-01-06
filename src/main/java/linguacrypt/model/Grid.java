package linguacrypt.model;

import java.util.Random;

public class Grid {
    private int size;
    private Card[][] grid;

    public Grid(int size) {
        this.size = size;
        this.grid = new Card[size][size];
    }

    public void initGrid() {
        BankCard bankCard = new BankCard();
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String randomWord = bankCard.getWordsDefault().get(random.nextInt(bankCard.getWordsDefault().size()));
                grid[i][j] = new Card(randomWord, "url_to_image");
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
                System.out.print(grid[i][j].getWord() + " ");
            }
            System.out.println();
        }
    }
}
