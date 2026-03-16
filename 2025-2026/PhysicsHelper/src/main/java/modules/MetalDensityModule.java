package modules;

import core.PhysicsModule;
import settings.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Модуль для отображения справочных данных о плотности металлов.
 */
public class MetalDensityModule implements PhysicsModule {
    private final Settings settings;
    private final int fontSize;

    private static final Object[][] DENSITY_DATA = {
            {"Литий", "534"}, {"Калий", "862"}, {"Натрий", "971"},
            {"Кальций", "1550"}, {"Магний", "1738"}, {"Бериллий", "1848"},
            {"Цезий", "1873"}, {"Стронций", "2630"}, {"Алюминий", "2698"},
            {"Барий", "3510"}, {"Титан", "4540"}, {"Ванадий", "6110"},
            {"Сурьма", "6684"}, {"Цинк", "7133"}, {"Хром", "7190"},
            {"Марганец", "7440"}, {"Железо", "7874"}, {"Кобальт", "8900"},
            {"Никель", "8908"}, {"Медь", "8960"}, {"Висмут", "9800"},
            {"Молибден", "10220"}, {"Серебро", "10500"}, {"Свинец", "11340"},
            {"Ртуть", "13546"}, {"Тантал", "16650"}, {"Золото", "19300"},
            {"Вольфрам", "19300"}, {"Платина", "21450"}, {"Осмий", "22590"}
    };

    private static final String[] COLUMN_NAMES = {"Металл", "Плотность (кг/м³)"};

    public MetalDensityModule() {
        this.settings = Settings.getInstance();
        this.fontSize = settings.getGLOBAL_FONT_SIZE();
    }

    @Override
    public String getName() {
        return "Плотность металлов";
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
        JLabel header = new JLabel("Справочник металлов", SwingConstants.CENTER);
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
