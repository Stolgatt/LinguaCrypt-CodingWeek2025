package linguacrypt.model.Game;

import java.io.Serializable;
import java.util.List;

public class Theme implements Serializable {
    private String name;
    private List<String> words;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
} 