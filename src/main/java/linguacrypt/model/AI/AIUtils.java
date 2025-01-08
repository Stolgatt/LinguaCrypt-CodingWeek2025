package linguacrypt.model.AI;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AIUtils {

    /**
     * Calcule la similarité entre deux mots.
     * Implémentez ici un algorithme basé sur WordNet, ou utilisez une méthode simple.
     */
    public static double calculateWordSimilarity(String word, String hint) {
        // Implémentation basique pour commencer.
        if (word.equalsIgnoreCase(hint)) {
            return 1.0; // Correspondance parfaite.
        }
        if (word.toLowerCase().contains(hint.toLowerCase())) {
            return 0.8; // Correspondance partielle.
        }
        return 0.2; // Faible correspondance.
    }

    public static List<String> giveSynonym(String word){
        String url = "https://www.synonymo.fr/synonyme/" + word;
        List<String> synonyms = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();

            Element compteurElement = doc.select("h2#compteur").first();
            if (compteurElement == null){
                return synonyms;
            }
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(compteurElement.text());

            int numberOfSynonyms = 0;
            if (matcher.find()) {
                numberOfSynonyms = Integer.parseInt(matcher.group());
            }

            Elements synonymLinks = doc.select("ul.synos li a.word");
            int i = 0;
            for (Element link : synonymLinks) {
                String synonym = link.text().trim();
                if (!synonym.isEmpty() && !synonym.matches(".*\\d.*")) { // éviter les chiffres
                    synonyms.add(synonym);
                    i++;
                }
                if (i==numberOfSynonyms-1) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return synonyms;
    }
    public static List<String> test(String word) throws IOException {
        String url = "https://conceptnet.io/c/fr/" + word;  // Remplace par l'URL réelle

        // Charger la page avec Jsoup
        Document doc = Jsoup.connect(url).get();

        // Récupérer les synonymes en français
        List<String> synonyms = getSynonyms(doc,word);
        //System.out.println("Synonymes en français : " + synonyms);

        // Récupérer les termes liés en français
        //List<String> relatedTerms = getRelatedTerms(doc,word);
        //System.out.println("Termes liés en français : " + relatedTerms);
        return synonyms;
    }

    // Méthode pour extraire les synonymes en français
    public static List<String> getSynonyms(Document doc,String word) {
        List<String> synonyms = new ArrayList<>();

        // Sélectionner les éléments correspondant aux synonymes en français
        Elements synonymElements = doc.select("a[href^=/c/fr/]");  // Sélectionner tous les liens qui sont dans la langue française

        // Parcourir les &eacute;l&eacute;ments et r&eacute;cup&eacute;rer les mots en fran&ccedil;ais
        for (Element synonym : synonymElements) {
            //System.out.println(synonym);
            String synonymText = synonym.text();
            if (!synonymText.isEmpty() && synonym.parent().hasClass("term") && synonym.parent().hasClass("lang-fr") && !synonymText.contains(" ")) {
                if (!synonymText.contains(word) && !word.contains(synonymText) && !synonyms.contains(synonymText)) {
                    synonyms.add(synonymText);
                }
            }
        }

        return synonyms;
    }

    // Méthode pour extraire les termes liés en français
    public static List<String> getRelatedTerms(Document doc, String word) {
        List<String> relatedTerms = new ArrayList<>();

        // Sélectionner les éléments correspondant aux termes liés en français
        Elements relatedElements = doc.select("a[href^=/c/fr/]");

        // Parcourir les éléments et récupérer les mots en français
        for (Element related : relatedElements) {
            String relatedText = related.text();
            // Vérifier si l'élément est bien un terme lié en français
            if (!relatedText.isEmpty() && related.parent().hasClass("term") && related.parent().hasClass("lang-fr") && !relatedText.contains(" ")) {
                if (!relatedText.contains(word) && !word.contains(relatedText)) {

                     relatedTerms.add(relatedText);
                }
            }
        }

        return relatedTerms;
    }

}