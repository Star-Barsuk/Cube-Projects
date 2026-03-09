package modules;

import core.PhysicsModule;

import javax.swing.*;
import java.awt.*;

/**
 * Модуль для конвертации единиц силы.
 * Выполняет преобразование Ньютонов в кило-, мега- и гиганьютоны.
 *
 * @see PhysicsModule
 */
public class ForceModule implements PhysicsModule {
    private final int fontSize;

    /**
     * Создает модуль силы с указанным размером шрифта.
     *
     * @param fontSize базовый размер шрифта для элементов интерфейса
     */
    public ForceModule(int fontSize) {
        this.fontSize = fontSize;
    }

    @Override
    public String getName() {
        return "Сила (Ньютоны)";
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
        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
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
        Font mainFont = new Font("Segoe UI", Font.PLAIN, fontSize);

        JLabel knLabel = new JLabel("кН: -");
        JLabel mnLabel = new JLabel("МН: -");
        JLabel gnLabel = new JLabel("ГН: -");

        knLabel.setFont(mainFont);
        mnLabel.setFont(mainFont);
        gnLabel.setFont(mainFont);

        return new JLabel[]{knLabel, mnLabel, gnLabel};
    }

    /**
     * Создает кнопку конвертации с настроенным шрифтом.
     */
    private JButton createConvertButton() {
        JButton button = new JButton("Конвертировать");
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
                double value = Double.parseDouble(inputField.getText().replace(",", "."));

                // Обновление результатов конвертации
                resultLabels[0].setText(String.format("кН (кило): %.6f", value / 1_000));
                resultLabels[1].setText(String.format("МН (мега): %.6f", value / 1_000_000));
                resultLabels[2].setText(String.format("ГН (гига): %.9f", value / 1_000_000_000));

            } catch (NumberFormatException ex) {
                showErrorDialog(parentPanel, "Пожалуйста, введите числовое значение силы!");
            }
        });
    }

    /**
     * Собирает все компоненты интерфейса в единую панель.
     */
    private void assembleInterface(JPanel panel, JTextField inputField,
                                   JButton convertButton, JLabel[] resultLabels) {
        Font mainFont = new Font("Segoe UI", Font.PLAIN, fontSize);

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
