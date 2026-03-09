package modules;

import core.PhysicsModule;

import javax.swing.*;
import java.awt.*;

/**
 * Модуль для конвертации единиц температуры.
 * Выполняет преобразование Цельсия в Фаренгейты и Кельвины.
 *
 * @see PhysicsModule
 */
public class TempModule implements PhysicsModule {
    private final int fontSize;

    /**
     * Создает модуль температуры с указанным размером шрифта.
     *
     * @param fontSize базовый размер шрифта для элементов интерфейса
     */
    public TempModule(int fontSize) {
        this.fontSize = fontSize;
    }

    @Override
    public String getName() {
        return "Температура (°C)";
    }

    @Override
    public JPanel getInterface() {
        // Инициализация компонентов интерфейса
        JPanel panel = createMainPanel();
        JTextField inputField = createInputField();
        JLabel[] resultLabels = createResultLabels();
        JButton convertButton = createConvertButton();

        // Настройка обработчика конвертации
        setupConversionHandler(convertButton, inputField, resultLabels, panel);

        // Сборка интерфейса
        assembleInterface(panel, inputField, convertButton, resultLabels);

        return wrapPanel(panel);
    }

    /**
     * Создает основную панель с отступами и сеткой.
     */
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    /**
     * Создает поле ввода с настроенным шрифтом.
     */
    private JTextField createInputField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        return field;
    }

    /**
     * Создает массив меток для отображения результатов конвертации.
     */
    private JLabel[] createResultLabels() {
        Font resultFont = new Font("Segoe UI", Font.BOLD, fontSize + 2);

        JLabel fahrenheitLabel = new JLabel("Фаренгейт: -");
        JLabel kelvinLabel = new JLabel("Кельвин: -");

        fahrenheitLabel.setFont(resultFont);
        kelvinLabel.setFont(resultFont);

        return new JLabel[]{fahrenheitLabel, kelvinLabel};
    }

    /**
     * Создает кнопку конвертации с настроенным шрифтом.
     */
    private JButton createConvertButton() {
        JButton button = new JButton("Рассчитать");
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        return button;
    }

    /**
     * Настраивает обработчик события для кнопки конвертации.
     */
    private void setupConversionHandler(JButton button, JTextField inputField,
                                        JLabel[] resultLabels, JPanel parentPanel) {
        button.addActionListener(_ -> {
            try {
                double celsius = Double.parseDouble(inputField.getText().replace(",", "."));

                // Обновление результатов конвертации
                resultLabels[0].setText(String.format("Фаренгейт: %.2f °F", (celsius * 9/5) + 32));
                resultLabels[1].setText(String.format("Кельвин: %.2f K", celsius + 273.15));

            } catch (NumberFormatException ex) {
                showErrorDialog(parentPanel, "Пожалуйста, введите числовое значение температуры!");
            }
        });
    }

    /**
     * Собирает все компоненты интерфейса в единую панель.
     */
    private void assembleInterface(JPanel panel, JTextField inputField,
                                   JButton convertButton, JLabel[] resultLabels) {
        Font mainFont = new Font("Segoe UI", Font.PLAIN, fontSize);

        JLabel inputLabel = new JLabel("Введите Цельсии (°C):");
        inputLabel.setFont(mainFont);

        panel.add(inputLabel);
        panel.add(inputField);
        panel.add(convertButton);
        panel.add(new JSeparator());
        panel.add(resultLabels[0]);
        panel.add(resultLabels[1]);
    }

    /**
     * Отображает диалоговое окно с сообщением об ошибке.
     */
    private void showErrorDialog(JPanel parentPanel, String message) {
        JOptionPane.showMessageDialog(parentPanel, message, "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Оборачивает панель для предотвращения растягивания элементов.
     */
    private JPanel wrapPanel(JPanel panel) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.NORTH);
        return wrapper;
    }
}
