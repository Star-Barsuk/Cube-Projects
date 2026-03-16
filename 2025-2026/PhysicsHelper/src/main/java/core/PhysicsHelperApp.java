package core;

import modules.converters.ForceModule;
import modules.converters.PressureModule;
import modules.converters.TempModule;
import modules.tables.GaspDensityModule;
import modules.tables.LiquidDensityModule;
import modules.tables.MetalDensityModule;
import settings.Settings;
import settings.SettingsDialog;

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
     * Создает панель заголовка приложения с кнопкой настроек.
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

        // Левая часть с заголовком
        JLabel titleLabel = new JLabel("PhysicsHelper");
        titleLabel.setFont(new Font(settings.getFONT_FAMILY(), Font.BOLD, settings.getHEADER_FONT_SIZE()));
        titleLabel.setForeground(settings.getCOLOR_HEADER_FG());

        // Центральная часть с версией
        JLabel versionLabel = new JLabel("v1.0");
        versionLabel.setFont(new Font(settings.getFONT_FAMILY(), Font.PLAIN, settings.getDEFAULT_FONT_SIZE()));
        versionLabel.setForeground(settings.getCOLOR_VERSION_FG());

        // Правая часть с кнопкой настроек
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);

        JButton settingsButton = new JButton("⚙️ Настройки");
        settingsButton.setFont(new Font(settings.getFONT_FAMILY(), Font.PLAIN, settings.getDEFAULT_FONT_SIZE()));
        settingsButton.setForeground(settings.getCOLOR_HEADER_FG());
        settingsButton.setBackground(new Color(80, 80, 80));
        settingsButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        settingsButton.setFocusPainted(false);
        settingsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Добавляем обработчик для открытия окна настроек
        settingsButton.addActionListener(e -> {
            SettingsDialog dialog = new SettingsDialog(this, settings);
            dialog.setVisible(true);
        });

        // Эффект при наведении
        settingsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                settingsButton.setBackground(new Color(100, 100, 100));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                settingsButton.setBackground(new Color(80, 80, 80));
            }
        });

        rightPanel.add(settingsButton);

        // Собираем заголовок
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(titleLabel);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerPanel.setOpaque(false);
        centerPanel.add(versionLabel);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(centerPanel, BorderLayout.CENTER);
        headerPanel.add(rightPanel, BorderLayout.EAST);

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
