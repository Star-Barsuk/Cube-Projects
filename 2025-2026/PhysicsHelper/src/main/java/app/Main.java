package app;

import core.PhysicsHelperApp;
import settings.Settings;

import javax.swing.*;
import java.awt.*;

/**
 * Главный класс запуска приложения PhysicsHelper.
 * Отвечает за инициализацию и запуск приложения с обработкой ошибок.
 */
public class Main {

    private static final String APP_TITLE = "PhysicsHelper — Помощник по физике";
    private static final Settings settings = Settings.getInstance();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                configureUIManager();
                createAndShowGUI();
            } catch (Exception e) {
                handleFatalError(e);
            }
        });
    }

    /**
     * Настройка глобальных параметров UI компонентов
     */
    private static void configureUIManager() {
        Font defaultFont = new Font(settings.getFONT_FAMILY(), Font.PLAIN, settings.getGLOBAL_FONT_SIZE());
        Font boldFont = new Font(settings.getFONT_FAMILY(), Font.BOLD, settings.getGLOBAL_FONT_SIZE());
        Font smallFont = new Font(settings.getFONT_FAMILY(), Font.PLAIN, settings.getDEFAULT_FONT_SIZE());

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

        UIManager.put("Button.background", settings.getCOLOR_BUTTON_BG());
        UIManager.put("Panel.background", settings.getCOLOR_PANEL_BG());

        UIManager.put("OptionPane.messageFont", defaultFont);
        UIManager.put("OptionPane.buttonFont", boldFont);
    }

    /**
     * Создание и отображение главного окна приложения
     */
    private static void createAndShowGUI() {
        try {
            PhysicsHelperApp app = new PhysicsHelperApp();

            app.setTitle(APP_TITLE);
            app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            app.setSize(settings.getDEFAULT_WIDTH(), settings.getDEFAULT_HEIGHT());
            app.setMinimumSize(new Dimension(settings.getMIN_WIDTH(), settings.getMIN_HEIGHT()));

            app.setLocationRelativeTo(null);

            app.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    handleApplicationExit();
                }
            });

            app.setVisible(true);

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

        try {
            JOptionPane.showMessageDialog(
                    null,
                    errorMessage,
                    "Ошибка запуска",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            System.err.println(errorMessage);
            e.printStackTrace();
        }

        System.exit(1);
    }

    /**
     * Обработка корректного завершения приложения
     */
    private static void handleApplicationExit() {
        System.out.println("Завершение работы приложения...");
        System.out.println("Приложение успешно завершено.");
    }
}
