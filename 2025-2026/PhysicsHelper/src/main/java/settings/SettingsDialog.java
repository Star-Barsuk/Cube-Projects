package settings;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Диалоговое окно для настройки базового шрифта.
 */
public class SettingsDialog extends JDialog {
    private final Settings settings;
    private final JFrame parentFrame;

    private JComboBox<String> fontFamilyCombo;
    private JSpinner fontSizeSpinner;
    private JButton saveButton;
    private JButton cancelButton;

    public SettingsDialog(JFrame parent, Settings settings) {
        super(parent, "Настройки шрифта", true);
        this.parentFrame = parent;
        this.settings = settings;

        initComponents();
        layoutComponents();
        loadSettingsToForm();
        setupListeners();

        setSize(400, 180);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponents() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontFamilies = ge.getAvailableFontFamilyNames();

        Font defaultFont = new Font(settings.getFONT_FAMILY(), Font.PLAIN, settings.getDEFAULT_FONT_SIZE());

        fontFamilyCombo = new JComboBox<>(fontFamilies);
        fontFamilyCombo.setEditable(true);
        fontFamilyCombo.setFont(defaultFont);

        fontSizeSpinner = new JSpinner(new SpinnerNumberModel(settings.getGLOBAL_FONT_SIZE(), 8, 72, 1));
        fontSizeSpinner.setFont(defaultFont);

        saveButton = new JButton("Сохранить");
        saveButton.setFont(new Font(settings.getFONT_FAMILY(), Font.BOLD, settings.getDEFAULT_FONT_SIZE()));

        cancelButton = new JButton("Отмена");
        cancelButton.setFont(defaultFont);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(settings.getCOLOR_PANEL_BG());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Шрифт
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel fontLabel = new JLabel("Шрифт:");
        fontLabel.setFont(new Font(settings.getFONT_FAMILY(), Font.PLAIN, settings.getDEFAULT_FONT_SIZE()));
        mainPanel.add(fontLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        mainPanel.add(fontFamilyCombo, gbc);

        // Размер
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel sizeLabel = new JLabel("Размер:");
        sizeLabel.setFont(new Font(settings.getFONT_FAMILY(), Font.PLAIN, settings.getDEFAULT_FONT_SIZE()));
        mainPanel.add(sizeLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        mainPanel.add(fontSizeSpinner, gbc);

        // Кнопки
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(settings.getCOLOR_PANEL_BG());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadSettingsToForm() {
        fontFamilyCombo.setSelectedItem(settings.getFONT_FAMILY());
        fontSizeSpinner.setValue(settings.getGLOBAL_FONT_SIZE());
    }

    private void applySettings() {
        String selectedFont = (String) fontFamilyCombo.getSelectedItem();
        int selectedSize = (Integer) fontSizeSpinner.getValue();

        // Сохраняем настройки в объект Settings
        settings.setFONT_FAMILY(selectedFont);
        settings.setGLOBAL_FONT_SIZE(selectedSize);
    }

    private void setupListeners() {
        saveButton.addActionListener(e -> {
            applySettings();
            settings.saveToFile();

            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Настройки будут применены после перезапуска приложения.\n\nПерезапустить приложение сейчас?",
                    "Применение настроек",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

            if (result == JOptionPane.YES_OPTION) {
                restartApplication();
            }
        });

        cancelButton.addActionListener(e -> dispose());
    }

    private void restartApplication() {
        try {
            String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            String classpath = System.getProperty("java.class.path");
            String mainClass = "app.Main";

            ProcessBuilder processBuilder = new ProcessBuilder(
                    javaBin,
                    "-cp",
                    classpath,
                    mainClass
            );

            processBuilder.start();

            System.exit(0);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Не удалось перезапустить приложение. Пожалуйста, перезапустите его вручную.",
                    "Ошибка перезапуска",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
}
