package settings;

import java.awt.*;
import java.io.*;
import java.nio.file.*;
import com.google.gson.*;

/**
 * Централизованное хранилище настроек приложения.
 * Реализован как синглтон для доступа из всех модулей.
 */
public class Settings {
    private static Settings instance;
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Color.class, (JsonSerializer<Color>) (color, type, context) -> {
                JsonObject object = new JsonObject();
                object.addProperty("r", color.getRed());
                object.addProperty("g", color.getGreen());
                object.addProperty("b", color.getBlue());
                object.addProperty("a", color.getAlpha());
                return object;
            })
            .registerTypeAdapter(Color.class, (JsonDeserializer<Color>) (json, type, context) -> {
                JsonObject object = json.getAsJsonObject();
                return new Color(
                        object.get("r").getAsInt(),
                        object.get("g").getAsInt(),
                        object.get("b").getAsInt(),
                        object.get("a").getAsInt()
                );
            })
            .create();

    private static final String SETTINGS_FILE = "settings.json";

    // ========== НАСТРОЙКИ ШРИФТОВ ==========
    private String FONT_FAMILY = "Segoe UI";
    private int DEFAULT_FONT_SIZE = 16;
    private int GLOBAL_FONT_SIZE = 18;
    private int HEADER_FONT_SIZE = 20;
    private int TABLE_HEADER_FONT_SIZE = 18;
    private int TABLE_ROW_FONT_SIZE = 16;

    // ========== НАСТРОЙКИ ЦВЕТОВ ==========
    private Color COLOR_HEADER_BG = new Color(60, 60, 60);
    private Color COLOR_HEADER_FG = Color.WHITE;
    private Color COLOR_VERSION_FG = new Color(200, 200, 200);
    private Color COLOR_TABLE_SELECTION_BG = new Color(200, 220, 240);
    private Color COLOR_PANEL_BG = new Color(245, 245, 245);
    private Color COLOR_BUTTON_BG = new Color(240, 240, 240);

    // ========== НАСТРОЙКИ РАЗМЕРОВ ОКНА ==========
    private int DEFAULT_WIDTH = 900;
    private int DEFAULT_HEIGHT = 600;
    private int MIN_WIDTH = 700;
    private int MIN_HEIGHT = 500;

    // ========== НАСТРОЙКИ КОМПОНЕНТОВ ==========
    private int MENU_ITEM_HEIGHT = 40;
    private int SPLIT_DIVIDER_LOCATION = 250;
    private int SPLIT_DIVIDER_SIZE = 3;
    private int TABLE_ROW_HEIGHT_OFFSET = 10;
    private int PANEL_PADDING = 20;
    private int COMPONENT_SPACING = 10;

    /**
     * Внутренний класс только для сохраняемых полей
     */
    private static class SavedSettings {
        String FONT_FAMILY;
        int GLOBAL_FONT_SIZE;

        // Пустой конструктор для Gson
        SavedSettings() {}

        SavedSettings(String fontFamily, int globalFontSize) {
            this.FONT_FAMILY = fontFamily;
            this.GLOBAL_FONT_SIZE = globalFontSize;
        }
    }

    private Settings() {
        loadFromFile();
    }

    public static synchronized Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    /**
     * Загрузка настроек из файла.
     * Загружаем только то, что сохраняем.
     */
    private void loadFromFile() {
        try {
            Path path = Paths.get(SETTINGS_FILE);
            if (Files.exists(path)) {
                String content = new String(Files.readAllBytes(path));
                SavedSettings saved = gson.fromJson(content, SavedSettings.class);
                if (saved != null) {
                    if (saved.FONT_FAMILY != null) {
                        this.FONT_FAMILY = saved.FONT_FAMILY;
                    }
                    if (saved.GLOBAL_FONT_SIZE > 0) {
                        this.GLOBAL_FONT_SIZE = saved.GLOBAL_FONT_SIZE;
                        this.DEFAULT_FONT_SIZE = saved.GLOBAL_FONT_SIZE - 2;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки настроек: " + e.getMessage());
        }
    }

    /**
     * Сохранение настроек в файл.
     * Сохраняем ТОЛЬКО изменяемые параметры.
     */
    public void saveToFile() {
        try {
            SavedSettings saved = new SavedSettings(FONT_FAMILY, GLOBAL_FONT_SIZE);
            String json = gson.toJson(saved);
            Files.write(Paths.get(SETTINGS_FILE), json.getBytes());
            System.out.println("Настройки сохранены");
        } catch (IOException e) {
            System.err.println("Ошибка сохранения настроек: " + e.getMessage());
        }
    }

    // ========== СЕТТЕРЫ ==========

    public void setFONT_FAMILY(String fontFamily) {
        if (fontFamily != null && !fontFamily.trim().isEmpty()) {
            this.FONT_FAMILY = fontFamily;
        }
    }

    public void setGLOBAL_FONT_SIZE(int size) {
        if (size >= 8 && size <= 72) {
            this.GLOBAL_FONT_SIZE = size;
            this.DEFAULT_FONT_SIZE = size - 2;
        }
    }

    // ========== ГЕТТЕРЫ ==========

    public String getFONT_FAMILY() { return FONT_FAMILY; }
    public int getDEFAULT_FONT_SIZE() { return DEFAULT_FONT_SIZE; }
    public int getGLOBAL_FONT_SIZE() { return GLOBAL_FONT_SIZE; }
    public int getHEADER_FONT_SIZE() { return HEADER_FONT_SIZE; }
    public int getTABLE_HEADER_FONT_SIZE() { return TABLE_HEADER_FONT_SIZE; }
    public int getTABLE_ROW_FONT_SIZE() { return TABLE_ROW_FONT_SIZE; }

    public Color getCOLOR_HEADER_BG() { return COLOR_HEADER_BG; }
    public Color getCOLOR_HEADER_FG() { return COLOR_HEADER_FG; }
    public Color getCOLOR_VERSION_FG() { return COLOR_VERSION_FG; }
    public Color getCOLOR_TABLE_SELECTION_BG() { return COLOR_TABLE_SELECTION_BG; }
    public Color getCOLOR_PANEL_BG() { return COLOR_PANEL_BG; }
    public Color getCOLOR_BUTTON_BG() { return COLOR_BUTTON_BG; }

    public int getDEFAULT_WIDTH() { return DEFAULT_WIDTH; }
    public int getDEFAULT_HEIGHT() { return DEFAULT_HEIGHT; }
    public int getMIN_WIDTH() { return MIN_WIDTH; }
    public int getMIN_HEIGHT() { return MIN_HEIGHT; }

    public int getMENU_ITEM_HEIGHT() { return MENU_ITEM_HEIGHT; }
    public int getSPLIT_DIVIDER_LOCATION() { return SPLIT_DIVIDER_LOCATION; }
    public int getSPLIT_DIVIDER_SIZE() { return SPLIT_DIVIDER_SIZE; }
    public int getTABLE_ROW_HEIGHT_OFFSET() { return TABLE_ROW_HEIGHT_OFFSET; }
    public int getPANEL_PADDING() { return PANEL_PADDING; }
    public int getCOMPONENT_SPACING() { return COMPONENT_SPACING; }
}
