package modules;

import core.PhysicsModule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Модуль для отображения справочных данных о плотности металлов.
 * Представляет данные в виде таблицы с возможностью прокрутки.
 *
 * @see PhysicsModule
 */
public class MetalDensityModule implements PhysicsModule {
    private final int fontSize;

    /**
     * Данные о плотности металлов: [название, плотность]
     */
    private static final Object[][] DENSITY_DATA = {
            {"Осмий", "22500"}, {"Золото", "19300"}, {"Свинец", "11340"},
            {"Серебро", "10500"}, {"Медь", "8900"}, {"Железо", "7870"},
            {"Сталь", "7800"}, {"Алюминий", "2700"}
    };

    /**
     * Наименования колонок таблицы
     */
    private static final String[] COLUMN_NAMES = {"Металл", "Плотность (кг/м³)"};

    /**
     * Создает модуль плотности металлов с указанным размером шрифта.
     *
     * @param fontSize базовый размер шрифта для элементов интерфейса
     */
    public MetalDensityModule(int fontSize) {
        this.fontSize = fontSize;
    }

    @Override
    public String getName() {
        return "Плотность металлов";
    }

    @Override
    public JPanel getInterface() {
        // Создание основной панели
        JPanel panel = new JPanel(new BorderLayout());

        // Добавление заголовка
        panel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Добавление таблицы с данными
        panel.add(createTablePanel(), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Создает панель заголовка с названием справочника.
     */
    private JPanel createHeaderPanel() {
        JLabel header = new JLabel("Справочник металлов", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, fontSize + 4));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(header, BorderLayout.CENTER);

        return headerPanel;
    }

    /**
     * Создает панель с таблицей данных.
     */
    private JScrollPane createTablePanel() {
        JTable table = createDataTable();
        customizeTable(table);

        return new JScrollPane(table);
    }

    /**
     * Создает и настраивает таблицу с данными.
     */
    private JTable createDataTable() {
        DefaultTableModel model = new DefaultTableModel(DENSITY_DATA, COLUMN_NAMES) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Запрет редактирования ячеек
            }
        };

        return new JTable(model);
    }

    /**
     * Настраивает внешний вид таблицы.
     */
    private void customizeTable(JTable table) {
        // Настройка шрифта и высоты строк
        table.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        table.setRowHeight(fontSize + 10);

        // Настройка заголовков таблицы
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        table.getTableHeader().setPreferredSize(new Dimension(0, fontSize + 15));

        // Настройка выделения
        table.setSelectionBackground(new Color(200, 220, 240));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
