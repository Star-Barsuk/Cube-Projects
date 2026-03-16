package gui;

import data.GameLoader;
import gui.modules.CharacterPanel;
import gui.modules.MainTab;
import models.GameData;
import services.GameService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowManager {
    private static JFrame mainFrame;
    private static GameService gameService;
    private static GameLoader gameLoader;

    public static JFrame createWindow(String title,
                                      GameService service,
                                      GameLoader loader) {
        gameService = service;
        gameLoader = loader;

        mainFrame = new JFrame(title);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 2 / 3.0);
        int height = (int) (screenSize.height * 2 / 3.0);
        mainFrame.setSize(width, height);
        mainFrame.setLocationRelativeTo(null);

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                shutdown();
            }
        });

        JPanel mainPanel = createMainPanel();
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);

        return mainFrame;
    }

    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        MainTab mainTab = new MainTab(gameService);
        tabbedPane.addTab("Главная", mainTab);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        CharacterPanel characterPanel = new CharacterPanel();
        mainPanel.add(characterPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private static void shutdown() {
        System.out.println("\n🔄 Завершение приложения, сохраняем прогресс...");

        if (gameService != null && gameLoader != null) {
            GameData saveData = new GameData();
            saveData.setElements(gameService.getDiscoveredElements());

            gameLoader.saveGame(saveData);
        }

        mainFrame.dispose();
        System.exit(0);
    }
}
