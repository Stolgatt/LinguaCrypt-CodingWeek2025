package linguacrypt.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import linguacrypt.model.Theme;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class ThemeLoader {
    public static List<Theme> loadThemes() {
        Gson gson = new Gson();
        Type themeListType = new TypeToken<List<Theme>>(){}.getType();
        
        try (InputStream inputStream = ThemeLoader.class.getResourceAsStream("/themes.json");
             InputStreamReader reader = new InputStreamReader(inputStream)) {
            return gson.fromJson(reader, themeListType);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
} 