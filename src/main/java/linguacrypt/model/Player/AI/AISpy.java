package linguacrypt.model.Player.AI;


import linguacrypt.model.Game.Grid;
import linguacrypt.model.Game.Hint;
import linguacrypt.model.Player.Player;
import linguacrypt.model.statistique.PlayerStat;

import java.io.IOException;
import java.util.*;

public class AISpy extends Player {

    private Random random;
    private int teamID;
    public AISpy(int teamID){
        super("Spy",true,"",new PlayerStat());
        this.random = new Random();
        this.teamID = teamID;
    }

    public Hint generateHint(Grid grid) throws IOException {
        //List<String> remainingWords = Arrays.asList("chat", "chien", "arbre", "berger");
        List<String> remainingWords = grid.getRemainingWordsForTeam(teamID);
        // Map pour stocker le comptage des synonymes
        Map<String, Integer> synonymCount = new HashMap<>();
        // Map pour stocker les mots qui utilisent un synonyme particulier
        Map<String, Set<String>> synonymReferences = new HashMap<>();

        // Parcours des mots restants
        for (String word : remainingWords) {
            System.out.println("Word : " + word);
            word = word.toLowerCase();
            List<String> synonyms = AIUtils.test(word);  // suppose que cette méthode te retourne les synonymes du mot
            if (synonyms == null) {
                continue;
            }
            // Parcours des synonymes pour chaque mot
            for (String synonym : synonyms) {
                // Mise à jour du comptage des synonymes
                synonymCount.put(synonym, synonymCount.getOrDefault(synonym, 0) + 1);

                // Mise à jour des mots qui utilisent ce synonyme
                synonymReferences
                        .computeIfAbsent(synonym, k -> new HashSet<>())
                        .add(word);
            }
        }
        String mostFrequentSynonym = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : synonymCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostFrequentSynonym = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        if (mostFrequentSynonym != null) {
            System.out.println("Le synonyme qui apparaît le plus souvent est '" + mostFrequentSynonym + "' avec " + maxCount + " occurrences.");
            System.out.println("Les mots qui font référence à ce synonyme sont : " + synonymReferences.get(mostFrequentSynonym));
        } else {
            System.out.println("Aucun synonyme trouvé.");
        }
    return new Hint(mostFrequentSynonym, maxCount);
    }
}