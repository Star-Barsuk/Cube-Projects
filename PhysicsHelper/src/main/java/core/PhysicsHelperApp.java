package core;

import modules.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Главное окно приложения PhysicsHelper.
 * Управляет отображением вкладок и модулей приложения.
 *
 * @see JFrame
 */
public class PhysicsHelperApp extends JFrame {
    /** Панель с вкладками для переключения между категориями модулей */
    private JTabbedPane tabbedPane;

    /** Глобальный размер шрифта для всех модулей */
    private static final int GLOBAL_FONT_SIZE = 16;

    /**
     * Конструктор главного окна приложения.
     * Инициализирует компоненты, размещает их и настраивает обработчики событий.
     */
    public PhysicsHelperApp() {
        initComponents();
        layoutComponents();
        setupListeners();
    }

    /**
     * Инициализирует основные компоненты интерфейса.
     * Создает вкладки и наполняет их модулями.
     */
    private void initComponents() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Создание вкладок с модулями
        createFirstTab();  // Вкладка "Конвертеры величин"
        createSecondTab(); // Вкладка "Справочные таблицы"

        // Выбор первой вкладки по умолчанию
        if (tabbedPane.getTabCount() > 0) {
            tabbedPane.setSelectedIndex(0);
        }
    }

    /**
     * Создает первую вкладку с модулями конвертации величин.
     */
    private void createFirstTab() {
        List<PhysicsModule> converterModules = new ArrayList<>();
        converterModules.add(new PressureModule(GLOBAL_FONT_SIZE));
        converterModules.add(new ForceModule(GLOBAL_FONT_SIZE));
        converterModules.add(new TempModule(GLOBAL_FONT_SIZE));

        JPanel tabPanel = createTabPanel(converterModules);
        tabbedPane.addTab("Конвертеры величин", tabPanel);
    }

    /**
     * Создает вторую вкладку со справочными таблицами.
     */
    private void createSecondTab() {
        List<PhysicsModule> referenceModules = new ArrayList<>();
        referenceModules.add(new LiquidDensityModule(GLOBAL_FONT_SIZE));
        referenceModules.add(new MetalDensityModule(GLOBAL_FONT_SIZE));

        JPanel tabPanel = createTabPanel(referenceModules);
        tabbedPane.addTab("Справочные таблицы", tabPanel);
    }

    /**
     * Создает панель для вкладки со списком модулей и областью контента.
     *
     * @param moduleList список модулей для отображения на вкладке
     * @return скомпонованная панель вкладки
     */
    private JPanel createTabPanel(List<PhysicsModule> moduleList) {
        JPanel tabPanel = new JPanel(new BorderLayout());
        JList<String> tabMenuList = createModuleList(moduleList);
        JPanel tabContentPanel = createContentPanel(moduleList);
        JSplitPane tabSplitPane = createSplitPane(tabMenuList, tabContentPanel);

        setupModuleSelectionHandler(tabMenuList, tabContentPanel, moduleList);

        tabPanel.add(tabSplitPane, BorderLayout.CENTER);
        return tabPanel;
    }

    /**
     * Создает список модулей для навигации.
     */
    private JList<String> createModuleList(List<PhysicsModule> moduleList) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (PhysicsModule module : moduleList) {
            listModel.addElement(module.getName());
        }

        JList<String> list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        list.setFixedCellHeight(40);
        list.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return list;
    }

    /**
     * Создает панель контента с переключением между модулями.
     */
    private JPanel createContentPanel(List<PhysicsModule> moduleList) {
        JPanel contentPanel = new JPanel(new CardLayout());

        for (PhysicsModule module : moduleList) {
            contentPanel.add(module.getInterface(), module.getName());
        }

        return contentPanel;
    }

    /**
     * Создает разделитель между списком модулей и областью контента.
     */
    private JSplitPane createSplitPane(JList<String> menuList, JPanel contentPanel) {
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(menuList),
                contentPanel
        );
        splitPane.setDividerLocation(250);
        splitPane.setDividerSize(3);

        return splitPane;
    }

    /**
     * Настраивает обработчик выбора элемента в списке модулей.
     */
    private void setupModuleSelectionHandler(JList<String> menuList, JPanel contentPanel,
                                             List<PhysicsModule> moduleList) {
        menuList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = menuList.getSelectedValue();
                if (selected != null) {
                    CardLayout layout = (CardLayout) contentPanel.getLayout();
                    layout.show(contentPanel, selected);
                }
            }
        });

        if (!moduleList.isEmpty()) {
            menuList.setSelectedIndex(0);
        }
    }

    /**
     * Размещает компоненты в главном окне.
     */
    private void layoutComponents() {
        JPanel headerPanel = createHeaderPanel();

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Создает панель заголовка приложения.
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(60, 60, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("PhysicsHelper");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        JLabel versionLabel = new JLabel("v1.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(200, 200, 200));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(versionLabel, BorderLayout.EAST);

        return headerPanel;
    }

    /**
     * Настраивает обработчики событий для главного окна.
     */
    private void setupListeners() {
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex >= 0) {
                String tabTitle = tabbedPane.getTitleAt(selectedIndex);
                setTitle("PhysicsHelper — " + tabTitle);
            }
        });
    }
}
