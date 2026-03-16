package core;

import modules.*;
import settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Главное окно приложения PhysicsHelper.
 * Управляет отображением вкладок и модулей приложения.
 */
public class PhysicsHelperApp extends JFrame {
    private JTabbedPane tabbedPane;
    private final Settings settings;

    public PhysicsHelperApp() {
        settings = Settings.getInstance();
        initComponents();
        layoutComponents();
        setupListeners();
    }

    /**
     * Инициализирует основные компоненты интерфейса.
     */
    private void initComponents() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font(settings.getFONT_FAMILY(), Font.BOLD, settings.getGLOBAL_FONT_SIZE()));

        createFirstTab();
        createSecondTab();

        if (tabbedPane.getTabCount() > 0) {
            tabbedPane.setSelectedIndex(0);
        }
    }

    /**
     * Создает первую вкладку с модулями конвертации величин.
     */
    private void createFirstTab() {
        List<PhysicsModule> converterModules = new ArrayList<>();
        converterModules.add(new PressureModule());
        converterModules.add(new ForceModule());
        converterModules.add(new TempModule());

        JPanel tabPanel = createTabPanel(converterModules);
        tabbedPane.addTab("Конвертеры величин", tabPanel);
    }

    /**
     * Создает вторую вкладку со справочными таблицами.
     */
    private void createSecondTab() {
        List<PhysicsModule> referenceModules = new ArrayList<>();
        referenceModules.add(new LiquidDensityModule());
        referenceModules.add(new MetalDensityModule());
        referenceModules.add(new GaspDensityModule());

        JPanel tabPanel = createTabPanel(referenceModules);
        tabbedPane.addTab("Справочные таблицы", tabPanel);
    }

    /**
     * Создает панель для вкладки со списком модулей и областью контента.
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
        list.setFont(new Font(settings.getFONT_FAMILY(), Font.PLAIN, settings.getGLOBAL_FONT_SIZE()));
        list.setFixedCellHeight(settings.getMENU_ITEM_HEIGHT());
        list.setBorder(BorderFactory.createEmptyBorder(
                settings.getCOMPONENT_SPACING(),
                settings.getCOMPONENT_SPACING(),
                settings.getCOMPONENT_SPACING(),
                settings.getCOMPONENT_SPACING()
        ));

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
        splitPane.setDividerLocation(settings.getSPLIT_DIVIDER_LOCATION());
        splitPane.setDividerSize(settings.getSPLIT_DIVIDER_SIZE());

        return splitPane;
    }

    /**
     * Настраивает обработчик выбора элемента в списке модулей.
     */
    private void setupModuleSelectionHandler(JList<String> menuList,
                                             JPanel contentPanel,
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
        headerPanel.setBackground(settings.getCOLOR_HEADER_BG());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(
                settings.getCOMPONENT_SPACING(),
                15,
                settings.getCOMPONENT_SPACING(),
                15
        ));

        JLabel titleLabel = new JLabel("PhysicsHelper");
        titleLabel.setFont(new Font(settings.getFONT_FAMILY(), Font.BOLD, settings.getHEADER_FONT_SIZE()));
        titleLabel.setForeground(settings.getCOLOR_HEADER_FG());

        JLabel versionLabel = new JLabel("v1.0");
        versionLabel.setFont(new Font(settings.getFONT_FAMILY(), Font.PLAIN, settings.getDEFAULT_FONT_SIZE()));
        versionLabel.setForeground(settings.getCOLOR_VERSION_FG());

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
