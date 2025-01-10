package linguacrypt.model.players.AI;

import linguacrypt.model.game.Grid;
import linguacrypt.model.game.Hint;
import linguacrypt.model.players.Player;
import linguacrypt.model.statistique.PlayerStat;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class AIAgent extends Player {
    private int teamID;
    private ArrayList<String> lastGuess = new ArrayList<>();
    public AIAgent(int teamID){
        super("Agent",false,"",new PlayerStat());
        this.teamID = teamID;
    }
    public List<String> tryFindHint(Hint hint, Grid grid) throws IOException {
        List<String> wordList = grid.getRemainingWords();
        int count = hint.getCount();
        String wordHint = hint.getWord();

        List<String> synonyms = AIUtils.test(wordHint);
        int found = 0;
        List<String> wordFound = new ArrayList<>();
        for (String word : wordList) {
            if (synonyms.contains(word)) {
                found++;
                wordFound.add(word);
            }
            if (AIUtils.test(word.toLowerCase()).contains(wordHint.toLowerCase())) {
                found++;
                wordFound.add(word);
            }
        }
        return wordFound;
    }
    public ArrayList<String> getLastGuess() {
        return lastGuess;}
    public void addGuess(String guess) {
        lastGuess.add(guess);
    }
    public void clearLastGuess() {
        lastGuess.clear();
    }
    public String guessToString(){
        String s = "";
        for (String word : lastGuess) {
            s += word + ", ";
        }
        if (!s.isEmpty()){
            s = s.substring(0, s.length()-2);
        }
        return s;
    }

}