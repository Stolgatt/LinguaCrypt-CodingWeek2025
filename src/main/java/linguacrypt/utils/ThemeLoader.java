package linguacrypt.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import linguacrypt.model.Theme;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class ThemeLoader {
    private static final String THEME_SAVE_FILE_PATH = "themes.dat";
    private static final String DEFAULT_THEME_FILE_PATH = "/themes.json";

    public static List<Theme> loadThemes() {
        // Try to load themes from the .dat file first
        List<Theme> themes = loadThemesFromDatFile();
        if (themes.isEmpty()) {
            // If the .dat file does not exist or is empty, load from the JSON file
            themes = loadThemesFromJsonFile();
        }
        return themes;
    }

    private static List<Theme> loadThemesFromJsonFile() {
        Gson gson = new Gson();
        Type themeListType = new TypeToken<List<Theme>>(){}.getType();
        
        try (InputStream inputStream = ThemeLoader.class.getResourceAsStream(DEFAULT_THEME_FILE_PATH);
             InputStreamReader reader = new InputStreamReader(inputStream)) {
            return gson.fromJson(reader, themeListType);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private static List<Theme> loadThemesFromDatFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(THEME_SAVE_FILE_PATH))) {
            return (List<Theme>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return List.of(); // Return an empty list if the file does not exist or cannot be read
        }
    }

    public static void addTheme(Theme theme) {
        List<Theme> themes = loadThemes();
        themes.add(theme);
        saveThemes(themes);
    }

    public static void updateTheme(Theme theme) {
        List<Theme> themes = loadThemes();
        for (int i = 0; i < themes.size(); i++) {
            if (themes.get(i).getName().equals(theme.getName())) {
                themes.set(i, theme);
                break;
            }
        }
        saveThemes(themes);
    }

    private static void saveThemes(List<Theme> themes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(THEME_SAVE_FILE_PATH))) {
            oos.writeObject(themes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Theme getThemeByName(String name) {
        List<Theme> themes = loadThemes();
        for (Theme theme : themes) {
            if (theme.getName().equals(name)) {
                return theme;
            }
        }
        return null;
    }
} 