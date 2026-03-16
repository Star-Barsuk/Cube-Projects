package modules.tables;

import core.PhysicsModule;
import settings.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Модуль для отображения справочных данных о плотности жидкостей.
 */
public class LiquidDensityModule implements PhysicsModule {
    private final Settings settings;
    private final int fontSize;

    private static final Object[][] DENSITY_DATA = {
            {"Бензин", "730"}, {"Эфир", "714"}, {"Спирт", "789"},
            {"Ацетон", "791"}, {"Керосин", "810"}, {"Нефть", "850"},
            {"Дизель", "850"}, {"Масло машинное", "910"}, {"Масло подсолнечное", "920"},
            {"Вода", "1000"}, {"Морская вода", "1025"}, {"Молоко", "1030"},
            {"Тормозная жидкость", "1040"}, {"Уксусная кислота", "1049"}, {"Азотная кислота", "1510"},
            {"Глицерин", "1260"}, {"Хлороформ", "1483"}, {"Серная кислота", "1840"},
            {"Ртуть", "13546"}, {"Бром", "3102"}
    };

    private static final String[] COLUMN_NAMES = {"Жидкость", "Плотность (кг/м³)"};

    public LiquidDensityModule() {
        this.settings = Settings.getInstance();
        this.fontSize = settings.getGLOBAL_FONT_SIZE();
    }

    @Override
    public String getName() {
        return "Плотность жидкостей";
    }

    @Override
    public JPanel getInterface() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(settings.getCOLOR_PANEL_BG());

        panel.add(createHeaderPanel(), BorderLayout.NORTH);
        panel.add(createTablePanel(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createHeaderPanel() {
        JLabel header = new JLabel("Справочник жидкостей", SwingConstants.CENTER);
        header.setFont(new Font(settings.getFONT_FAMILY(), Font.BOLD, fontSize + 4));
        header.setBorder(BorderFactory.createEmptyBorder(
                settings.getCOMPONENT_SPACING(), 0,
                settings.getCOMPONENT_SPACING(), 0
        ));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(settings.getCOLOR_PANEL_BG());
        headerPanel.add(header, BorderLayout.CENTER);

        return headerPanel;
    }

    private JScrollPane createTablePanel() {
        JTable table = createDataTable();
        customizeTable(table);

        return new JScrollPane(table);
    }

    private JTable createDataTable() {
        DefaultTableModel model = new DefaultTableModel(DENSITY_DATA, COLUMN_NAMES) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        return new JTable(model);
    }

    private void customizeTable(JTable table) {
        table.setFont(new Font(settings.getFONT_FAMILY(), Font.PLAIN, settings.getTABLE_ROW_FONT_SIZE()));
        table.setRowHeight(settings.getTABLE_ROW_FONT_SIZE() + settings.getTABLE_ROW_HEIGHT_OFFSET());

        table.getTableHeader().setFont(new Font(settings.getFONT_FAMILY(), Font.BOLD, settings.getTABLE_HEADER_FONT_SIZE()));
        table.getTableHeader().setPreferredSize(new Dimension(0,
                settings.getTABLE_HEADER_FONT_SIZE() + settings.getTABLE_ROW_HEIGHT_OFFSET()));

        table.setSelectionBackground(settings.getCOLOR_TABLE_SELECTION_BG());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
