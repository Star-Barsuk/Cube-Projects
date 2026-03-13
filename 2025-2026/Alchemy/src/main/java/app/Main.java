package app;

import data.GameLoader;
import models.GameData;
import services.GameService;
import ui.GameConsole;

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
        GameConsole gameConsole = new GameConsole(gameService);

        gameConsole.start();

        saveGameProgress(loader, gameService);
    }

    private static void saveGameProgress(GameLoader loader, GameService gameService) {
        GameData saveData = new GameData();
        saveData.setElements(gameService.getDiscoveredElements());

        if (!loader.saveGame(saveData)) {
            System.err.println("⚠ Не удалось сохранить прогресс игры");
        } else {
            System.out.println("\n✨ Прогресс успешно сохранен!");
        }
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
