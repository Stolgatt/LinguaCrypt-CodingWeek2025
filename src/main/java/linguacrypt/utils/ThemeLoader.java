package linguacrypt.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import linguacrypt.model.Theme;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class ThemeLoader {
    private static final String WORDS_THEME_SAVE_FILE_PATH = "wordsThemes.dat"; // File to save word themes
    private static final String PICT_THEME_SAVE_FILE_PATH = "pictThemes.dat"; // File to save picture themes
    private static final String DEFAULT_WORDS_THEME_FILE_PATH = "/wordThemes.json"; // File for word themes
    private static final String DEFAULT_PICTS_THEME_FILE_PATH = "/pictThemes.json"; // File for picture themes

    public static List<Theme> loadThemes(int gameMode) {
        if (gameMode == 0) { // Word Game Mode
            List<Theme> themes = loadThemesFromDatFile(gameMode);
            if (themes.isEmpty()) {
                themes = loadThemesFromJsonFile(gameMode);
            }
            return themes;
        } else if (gameMode == 1) { // Picture Game Mode
            List<Theme> themes = loadThemesFromDatFile(gameMode);
            if (themes.isEmpty()) {
                themes = loadThemesFromJsonFile(gameMode);    
            }
            return themes;
        }
        return List.of();
    }

    private static List<Theme> loadThemesFromJsonFile(int gameMode) {
        Gson gson = new Gson();
        Type themeListType = new TypeToken<List<Theme>>(){}.getType();

        String filePath = (gameMode == 1) ? DEFAULT_PICTS_THEME_FILE_PATH : DEFAULT_WORDS_THEME_FILE_PATH;

        try (InputStream inputStream = ThemeLoader.class.getResourceAsStream(filePath);
             InputStreamReader reader = new InputStreamReader(inputStream)) {
            return gson.fromJson(reader, themeListType);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return an empty list if an error occurs while reading the JSON file
        }
    }

    private static List<Theme> loadThemesFromDatFile(int gameMode) {
        String filePath = (gameMode == 1) ? PICT_THEME_SAVE_FILE_PATH : WORDS_THEME_SAVE_FILE_PATH;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Theme>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return List.of(); // Return an empty list if the .dat file is not found or cannot be read
        }
    }

    public static void addTheme(Theme theme, int gameMode) {
        List<Theme> themes = loadThemes(gameMode);
        themes.add(theme);
        saveThemes(themes, gameMode);
    }

    public static void updateTheme(Theme theme, int gameMode) {
        List<Theme> themes = loadThemes(gameMode);
        for (int i = 0; i < themes.size(); i++) {
            if (themes.get(i).getName().equals(theme.getName())) {
                themes.set(i, theme);
                break;
            }
        }
        saveThemes(themes, gameMode);
    }

    private static void saveThemes(List<Theme> themes, int gameMode) {
        String filePath = (gameMode == 1) ? PICT_THEME_SAVE_FILE_PATH : WORDS_THEME_SAVE_FILE_PATH;

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(themes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Theme getThemeByName(String name, int gameMode) {
        List<Theme> themes = loadThemes(gameMode);
        for (Theme theme : themes) {
            if (theme.getName().equals(name)) {
                return theme;
            }
        }
        return null; // Return null if the theme is not found
    }
}


    