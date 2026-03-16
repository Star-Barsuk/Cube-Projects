package modules.tables;

import core.PhysicsModule;
import settings.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Модуль для отображения справочных данных о плотности газов.
 */
public class GaspDensityModule implements PhysicsModule {
    private final Settings settings;
    private final int fontSize;

    private static final Object[][] DENSITY_DATA = {
            {"Водород", "0.09"}, {"Гелий", "0.18"}, {"Метан", "0.72"},
            {"Неон", "0.90"}, {"Аммиак", "0.77"}, {"Азот", "1.25"},
            {"Этилен", "1.26"}, {"Воздух", "1.29"}, {"Угарный газ", "1.25"},
            {"Кислород", "1.43"}, {"Углекислый газ", "1.98"}, {"Закись азота", "1.98"},
            {"Пропан", "2.01"}, {"Диоксид серы", "2.93"}, {"Бутан", "2.70"},
            {"Озон", "2.14"}, {"Хлор", "3.21"}, {"Аргон", "1.78"},
            {"Криптон", "3.75"}, {"Ксенон", "5.89"}
    };

    private static final String[] COLUMN_NAMES = {"Газ", "Плотность (кг/м³)"};

    public GaspDensityModule() {
        this.settings = Settings.getInstance();
        this.fontSize = settings.getGLOBAL_FONT_SIZE();
    }

    @Override
    public String getName() {
        return "Плотность газов";
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
        JLabel header = new JLabel("Справочник газов", SwingConstants.CENTER);
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
