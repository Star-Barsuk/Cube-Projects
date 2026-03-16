package modules;

import core.PhysicsModule;
import settings.Settings;

import javax.swing.*;
import java.awt.*;

/**
 * Модуль для конвертации единиц силы.
 */
public class ForceModule implements PhysicsModule {
    private final Settings settings;
    private final int fontSize;

    public ForceModule() {
        this.settings = Settings.getInstance();
        this.fontSize = settings.getGLOBAL_FONT_SIZE();
    }

    @Override
    public String getName() {
        return "Сила (Ньютоны)";
    }

    @Override
    public JPanel getInterface() {
        JPanel panel = createMainPanel();
        JTextField inputField = createInputField();
        JLabel[] resultLabels = createResultLabels();
        JButton convertButton = createConvertButton();

        setupConversionHandler(convertButton, inputField, resultLabels, panel);
        assembleInterface(panel, inputField, convertButton, resultLabels);

        return wrapPanel(panel);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 1,
                settings.getCOMPONENT_SPACING(),
                settings.getCOMPONENT_SPACING()));
        panel.setBorder(BorderFactory.createEmptyBorder(
                settings.getPANEL_PADDING(),
                settings.getPANEL_PADDING(),
                settings.getPANEL_PADDING(),
                settings.getPANEL_PADDING()
        ));
        return panel;
    }

    private JTextField createInputField() {
        JTextField field = new JTextField();
        field.setFont(new Font(settings.getFONT_FAMILY(), Font.PLAIN, fontSize));
        return field;
    }

    private JLabel[] createResultLabels() {
        Font mainFont = new Font(settings.getFONT_FAMILY(), Font.PLAIN, fontSize);

        JLabel knLabel = new JLabel("кН: -");
        JLabel mnLabel = new JLabel("МН: -");
        JLabel gnLabel = new JLabel("ГН: -");

        knLabel.setFont(mainFont);
        mnLabel.setFont(mainFont);
        gnLabel.setFont(mainFont);

        return new JLabel[]{knLabel, mnLabel, gnLabel};
    }

    private JButton createConvertButton() {
        JButton button = new JButton("Конвертировать");
        button.setFont(new Font(settings.getFONT_FAMILY(), Font.BOLD, fontSize));
        return button;
    }

    private void setupConversionHandler(JButton button, JTextField inputField,
                                        JLabel[] resultLabels, JPanel parentPanel) {
        button.addActionListener(_ -> {
            try {
                double value = Double.parseDouble(inputField.getText().replace(",", "."));

                resultLabels[0].setText(String.format("кН (кило): %.6f", value / 1_000));
                resultLabels[1].setText(String.format("МН (мега): %.6f", value / 1_000_000));
                resultLabels[2].setText(String.format("ГН (гига): %.9f", value / 1_000_000_000));

            } catch (NumberFormatException ex) {
                showErrorDialog(parentPanel, "Пожалуйста, введите числовое значение силы!");
            }
        });
    }

    private void assembleInterface(JPanel panel, JTextField inputField,
                                   JButton convertButton, JLabel[] resultLabels) {
        Font mainFont = new Font(settings.getFONT_FAMILY(), Font.PLAIN, fontSize);

        JLabel inputLabel = new JLabel("Введите Ньютоны (Н):");
        inputLabel.setFont(mainFont);

        panel.add(inputLabel);
        panel.add(inputField);
        panel.add(convertButton);
        panel.add(new JSeparator());
        panel.add(resultLabels[0]);
        panel.add(resultLabels[1]);
        panel.add(resultLabels[2]);
    }

    private void showErrorDialog(JPanel parentPanel, String message) {
        JOptionPane.showMessageDialog(parentPanel, message, "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
    }

    private JPanel wrapPanel(JPanel panel) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.NORTH);
        return wrapper;
    }
}
