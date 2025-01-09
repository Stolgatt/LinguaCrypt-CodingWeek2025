package linguacrypt.model.Player.AI;

import linguacrypt.model.Game.Grid;
import linguacrypt.model.Game.Hint;
import linguacrypt.model.Player.Player;
import linguacrypt.model.statistique.PlayerStat;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class AIAgent extends Player {
    private int teamID;
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
}