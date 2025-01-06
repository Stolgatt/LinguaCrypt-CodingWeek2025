package linguacrypt.model;

public class Grid {
    private int size;
    private Card[][] grid;

    public Grid(int size) {
        this.size = size;
        this.grid = new Card[size][size];
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
}
