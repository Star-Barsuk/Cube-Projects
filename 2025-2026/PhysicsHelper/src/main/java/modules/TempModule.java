package modules;

import core.PhysicsModule;
import settings.Settings;

import javax.swing.*;
import java.awt.*;

/**
 * Модуль для конвертации единиц температуры.
 */
public class TempModule implements PhysicsModule {
    private final Settings settings;
    private final int fontSize;

    public TempModule() {
        this.settings = Settings.getInstance();
        this.fontSize = settings.getGLOBAL_FONT_SIZE();
    }

    @Override
    public String getName() {
        return "Температура (°C)";
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
        JPanel panel = new JPanel(new GridLayout(6, 1,
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
        Font resultFont = new Font(settings.getFONT_FAMILY(), Font.BOLD, fontSize + 2);

        JLabel fahrenheitLabel = new JLabel("Фаренгейт: -");
        JLabel kelvinLabel = new JLabel("Кельвин: -");

        fahrenheitLabel.setFont(resultFont);
        kelvinLabel.setFont(resultFont);

        return new JLabel[]{fahrenheitLabel, kelvinLabel};
    }

    private JButton createConvertButton() {
        JButton button = new JButton("Рассчитать");
        button.setFont(new Font(settings.getFONT_FAMILY(), Font.BOLD, fontSize));
        return button;
    }

    private void setupConversionHandler(JButton button, JTextField inputField,
                                        JLabel[] resultLabels, JPanel parentPanel) {
        button.addActionListener(_ -> {
            try {
                double celsius = Double.parseDouble(inputField.getText().replace(",", "."));

                resultLabels[0].setText(String.format("Фаренгейт: %.2f °F", (celsius * 9/5) + 32));
                resultLabels[1].setText(String.format("Кельвин: %.2f K", celsius + 273.15));

            } catch (NumberFormatException ex) {
                showErrorDialog(parentPanel, "Пожалуйста, введите числовое значение температуры!");
            }
        });
    }

    private void assembleInterface(JPanel panel, JTextField inputField,
                                   JButton convertButton, JLabel[] resultLabels) {
        Font mainFont = new Font(settings.getFONT_FAMILY(), Font.PLAIN, fontSize);

        JLabel inputLabel = new JLabel("Введите Цельсии (°C):");
        inputLabel.setFont(mainFont);

        panel.add(inputLabel);
        panel.add(inputField);
        panel.add(convertButton);
        panel.add(new JSeparator());
        panel.add(resultLabels[0]);
        panel.add(resultLabels[1]);
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
