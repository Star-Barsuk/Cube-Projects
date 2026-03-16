package settings;

import java.awt.*;

/**
 * Централизованное хранилище настроек приложения.
 * Реализован как синглтон для доступа из всех модулей.
 */
public class Settings {
    private static Settings instance;

    // ========== НАСТРОЙКИ ШРИФТОВ ==========
    private final String FONT_FAMILY = "Segoe UI";
    private final int DEFAULT_FONT_SIZE = 16;
    private final int GLOBAL_FONT_SIZE = 18;
    private final int HEADER_FONT_SIZE = 20;
    private final int TABLE_HEADER_FONT_SIZE = 18;
    private final int TABLE_ROW_FONT_SIZE = 16;

    // ========== НАСТРОЙКИ ЦВЕТОВ ==========
    private final Color COLOR_HEADER_BG = new Color(60, 60, 60);
    private final Color COLOR_HEADER_FG = Color.WHITE;
    private final Color COLOR_VERSION_FG = new Color(200, 200, 200);
    private final Color COLOR_TABLE_SELECTION_BG = new Color(200, 220, 240);
    private final Color COLOR_PANEL_BG = new Color(245, 245, 245);
    private final Color COLOR_BUTTON_BG = new Color(240, 240, 240);

    // ========== НАСТРОЙКИ РАЗМЕРОВ ОКНА ==========
    private final int DEFAULT_WIDTH = 900;
    private final int DEFAULT_HEIGHT = 600;
    private final int MIN_WIDTH = 700;
    private final int MIN_HEIGHT = 500;

    // ========== НАСТРОЙКИ КОМПОНЕНТОВ ==========
    private final int MENU_ITEM_HEIGHT = 40;
    private final int SPLIT_DIVIDER_LOCATION = 250;
    private final int SPLIT_DIVIDER_SIZE = 3;
    private final int TABLE_ROW_HEIGHT_OFFSET = 10;
    private final int PANEL_PADDING = 20;
    private final int COMPONENT_SPACING = 10;

    private Settings() {}

    public static synchronized Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    // ========== ГЕТТЕРЫ ШРИФТОВ ==========
    public String getFONT_FAMILY() { return FONT_FAMILY; }
    public int getDEFAULT_FONT_SIZE() { return DEFAULT_FONT_SIZE; }
    public int getGLOBAL_FONT_SIZE() { return GLOBAL_FONT_SIZE; }
    public int getHEADER_FONT_SIZE() { return HEADER_FONT_SIZE; }
    public int getTABLE_HEADER_FONT_SIZE() { return TABLE_HEADER_FONT_SIZE; }
    public int getTABLE_ROW_FONT_SIZE() { return TABLE_ROW_FONT_SIZE; }

    // ========== ГЕТТЕРЫ ЦВЕТОВ ==========
    public Color getCOLOR_HEADER_BG() { return COLOR_HEADER_BG; }
    public Color getCOLOR_HEADER_FG() { return COLOR_HEADER_FG; }
    public Color getCOLOR_VERSION_FG() { return COLOR_VERSION_FG; }
    public Color getCOLOR_TABLE_SELECTION_BG() { return COLOR_TABLE_SELECTION_BG; }
    public Color getCOLOR_PANEL_BG() { return COLOR_PANEL_BG; }
    public Color getCOLOR_BUTTON_BG() { return COLOR_BUTTON_BG; }

    // ========== ГЕТТЕРЫ РАЗМЕРОВ ОКНА ==========
    public int getDEFAULT_WIDTH() { return DEFAULT_WIDTH; }
    public int getDEFAULT_HEIGHT() { return DEFAULT_HEIGHT; }
    public int getMIN_WIDTH() { return MIN_WIDTH; }
    public int getMIN_HEIGHT() { return MIN_HEIGHT; }

    // ========== ГЕТТЕРЫ КОМПОНЕНТОВ ==========
    public int getMENU_ITEM_HEIGHT() { return MENU_ITEM_HEIGHT; }
    public int getSPLIT_DIVIDER_LOCATION() { return SPLIT_DIVIDER_LOCATION; }
    public int getSPLIT_DIVIDER_SIZE() { return SPLIT_DIVIDER_SIZE; }
    public int getTABLE_ROW_HEIGHT_OFFSET() { return TABLE_ROW_HEIGHT_OFFSET; }
    public int getPANEL_PADDING() { return PANEL_PADDING; }
    public int getCOMPONENT_SPACING() { return COMPONENT_SPACING; }
}
