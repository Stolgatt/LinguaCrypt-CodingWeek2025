package linguacrypt.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BankCard {
    private ArrayList<String> wordsDefault;

    public BankCard() {
        this.wordsDefault = new ArrayList<>();
        loadWordsFromFile(getClass().getResourceAsStream("/codenames.txt"));
    }

    //Load a txt file
    public void loadWordsFromFile(InputStream inputStream) {
        if (inputStream == null) {
            System.err.println("Le fichier n'a pas été trouvé.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                wordsDefault.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getWordsDefault() {return wordsDefault;}
}
