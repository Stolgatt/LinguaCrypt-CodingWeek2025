package linguacrypt.model.game;

public class Hint {
    private String word;
    private int count;

    public Hint(String word, int count) {
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return word + " (" + count + ")";
    }
}