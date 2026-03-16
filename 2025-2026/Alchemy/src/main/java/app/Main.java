package app;

import data.GameLoader;
import gui.WindowManager;
import models.GameData;
import services.GameService;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        GameLoader loader = new GameLoader();
        Optional<GameData> gameDataOptional = loader.loadGame();

        if (gameDataOptional.isEmpty()) {
            showFatalError();
            System.exit(1);
        }

        GameData gameData = gameDataOptional.get();
        GameService gameService = new GameService(gameData);

        javax.swing.SwingUtilities.invokeLater(() -> {
            WindowManager.createWindow("Алхимия", gameService, loader);
        });

        // GameConsole gameConsole = new GameConsole(gameService);
        // gameConsole.start();
    }

    private static void showFatalError() {
        System.err.println("""
            
            ⚠ КРИТИЧЕСКАЯ ОШИБКА
            ════════════════════════════════════════
            Не удалось загрузить данные игры.
            
            Возможные причины:
            • Файл elements.json отсутствует в resources
            • Файл elements.json поврежден
            • Недостаточно прав для чтения файла
            ════════════════════════════════════════
            
            Игра не может быть запущена.
            """);
    }
}
