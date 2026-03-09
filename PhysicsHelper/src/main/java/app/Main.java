package app;

import core.PhysicsHelperApp;
import javax.swing.*;
import java.awt.*;

/**
 * Главный класс запуска приложения PhysicsHelper.
 * Отвечает за инициализацию и запуск приложения с обработкой ошибок.
 */
public class Main {

    // Константы для настройки приложения
    private static final String APP_TITLE = "PhysicsHelper — Помощник по физике";
    private static final int DEFAULT_WIDTH = 900;
    private static final int DEFAULT_HEIGHT = 600;
    private static final int MIN_WIDTH = 700;
    private static final int MIN_HEIGHT = 500;

    static void main() {
        // Запускаем приложение в потоке обработки событий Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Настройка внешнего вида приложения
                configureLookAndFeel();

                // Настройка глобальных параметров UI
                configureUIManager();

                // Создание и запуск основного окна
                createAndShowGUI();

            } catch (Exception e) {
                handleFatalError(e);
            }
        });
    }

    /**
     * Настройка Look and Feel для разных платформ
     */
    private static void configureLookAndFeel() {
        try {
            // Пытаемся установить системный стиль
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Дополнительная настройка для разных ОС
            String osName = System.getProperty("os.name").toLowerCase();

            if (osName.contains("mac")) {
                // Специальные настройки для macOS
                System.setProperty("apple.awt.graphics.UseQuartz", "true");
                System.setProperty("apple.awt.textantialiasing", "true");
            } else if (osName.contains("windows")) {
                // Настройки для Windows
                UIManager.put("swing.boldMetal", Boolean.FALSE);
            }

        } catch (Exception e) {
            System.err.println("Не удалось установить системный стиль: " + e.getMessage());
            // Используем стандартный стиль Java
        }
    }

    /**
     * Настройка глобальных параметров UI компонентов
     */
    private static void configureUIManager() {
        // Настройка шрифтов для различных компонентов
        Font defaultFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font boldFont = new Font("Segoe UI", Font.BOLD, 14);
        Font smallFont = new Font("Segoe UI", Font.PLAIN, 12);

        // Применяем настройки к различным компонентам
        UIManager.put("Button.font", boldFont);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("TextArea.font", defaultFont);
        UIManager.put("ComboBox.font", defaultFont);
        UIManager.put("Table.font", defaultFont);
        UIManager.put("TableHeader.font", boldFont);
        UIManager.put("Menu.font", defaultFont);
        UIManager.put("MenuItem.font", defaultFont);
        UIManager.put("PopupMenu.font", defaultFont);
        UIManager.put("CheckBox.font", defaultFont);
        UIManager.put("RadioButton.font", defaultFont);
        UIManager.put("TabbedPane.font", defaultFont);
        UIManager.put("TitledBorder.font", boldFont);
        UIManager.put("ToolTip.font", smallFont);

        // Настройка цветов (опционально)
        UIManager.put("Button.background", new Color(240, 240, 240));
        UIManager.put("Panel.background", new Color(245, 245, 245));

        // Настройка диалоговых окон
        UIManager.put("OptionPane.messageFont", defaultFont);
        UIManager.put("OptionPane.buttonFont", boldFont);
    }

    /**
     * Создание и отображение главного окна приложения
     */
    private static void createAndShowGUI() {
        try {
            // Создаем главное окно приложения
            PhysicsHelperApp app = new PhysicsHelperApp();

            // Настраиваем параметры окна
            app.setTitle(APP_TITLE);
            app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            app.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            app.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));

            // Центрируем окно на экране
            app.setLocationRelativeTo(null);

            // Добавляем обработчик закрытия окна
            app.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    handleApplicationExit();
                }
            });

            // Показываем окно
            app.setVisible(true);

            // Логируем успешный запуск
            System.out.println("Приложение успешно запущено: " + new java.util.Date());

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании GUI: " + e.getMessage(), e);
        }
    }

    /**
     * Обработка фатальных ошибок при запуске
     */
    private static void handleFatalError(Exception e) {
        String errorMessage = String.format(
                "Критическая ошибка при запуске приложения:\n%s\n\n" +
                        "Пожалуйста, проверьте установку Java и перезапустите приложение.",
                e.getMessage()
        );

        // Пытаемся показать диалог ошибки
        try {
            JOptionPane.showMessageDialog(
                    null,
                    errorMessage,
                    "Ошибка запуска",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            // Если не удалось показать диалог, выводим в консоль
            System.err.println(errorMessage);
            e.printStackTrace();
        }

        // Завершаем приложение с кодом ошибки
        System.exit(1);
    }

    /**
     * Обработка корректного завершения приложения
     */
    private static void handleApplicationExit() {
        System.out.println("Завершение работы приложения...");

        // Здесь можно добавить сохранение настроек, закрытие ресурсов и т.д.

        System.out.println("Приложение успешно завершено.");
    }

    /**
     * Метод для проверки версии Java (опционально)
     */
    private static void checkJavaVersion() {
        String version = System.getProperty("java.version");
        System.out.println("Версия Java: " + version);

        // Проверяем минимальную версию (например, Java 8 или выше)
        if (version.startsWith("1.4") || version.startsWith("1.5") ||
                version.startsWith("1.6") || version.startsWith("1.7")) {
            System.err.println("Внимание: Рекомендуется Java 8 или выше!");
        }
    }

    /**
     * Статический блок инициализации
     */
    static {
        // Проверяем версию Java при загрузке класса
        checkJavaVersion();

        // Настраиваем прокси, если необходимо (для модулей, требующих интернет)
        // System.setProperty("http.proxyHost", "proxy.example.com");
        // System.setProperty("http.proxyPort", "8080");
    }
}
