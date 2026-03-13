package data;

import models.GameData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.nio.file.*;
import java.util.Optional;

public class GameLoader {
    private static final String SAVE_FILE = "elements-save.json";
    private static final String RESOURCE_FILE = "elements.json";

    private final Path savePath;
    private final ObjectMapper mapper;

    public GameLoader() {
        this.savePath = Paths.get(System.getProperty("user.dir"), SAVE_FILE);
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Загрузка данных: сначала пробует сохранение, если его нет - начальные данные
     */
    public Optional<GameData> loadGame() {
        // Всегда загружаем начальные данные для получения рецептов
        Optional<GameData> initial = loadInitialData();

        if (initial.isEmpty()) {
            return Optional.empty();
        }

        GameData gameData = initial.get();

        // Пробуем загрузить сохранение
        Optional<GameData> saved = loadFromFile(savePath);

        if (saved.isPresent()) {
            // Заменяем элементы из сохранения, но рецепты оставляем из начальных данных
            gameData.setElements(saved.get().getElements());
            System.out.println("✅ Загружено сохранение игры");
        } else {
            System.out.println("✅ Загружены начальные данные игры");
        }

        return Optional.of(gameData);
    }

    /**
     * Сохраняет игру в файл
     */
    public boolean saveGame(GameData gameData) {
        // Создаем копию только с элементами для сохранения
        GameData saveData = new GameData();
        saveData.setElements(gameData.getElements());
        // Рецепты не сохраняем - они всегда грузятся из resources

        return saveToFile(saveData, savePath);
    }

    /**
     * Загружает данные из файла
     */
    private Optional<GameData> loadFromFile(Path path) {
        if (!Files.exists(path)) {
            return Optional.empty();
        }

        try {
            GameData gameData = mapper.readValue(path.toFile(), GameData.class);
            return Optional.ofNullable(gameData);
        } catch (Exception e) {
            System.err.printf("⚠ Не удалось загрузить файл %s: %s%n",
                    path.getFileName(), e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Сохраняет данные в файл
     */
    private boolean saveToFile(GameData gameData, Path path) {
        try {
            mapper.writeValue(path.toFile(), gameData);
            System.out.printf("💾 Игра сохранена в файл: %s%n", path.toAbsolutePath());
            return true;
        } catch (Exception e) {
            System.err.printf("❌ Ошибка при сохранении файла %s: %s%n",
                    path.getFileName(), e.getMessage());
            return false;
        }
    }

    /**
     * Загружает начальные данные из resources
     */
    private Optional<GameData> loadInitialData() {
        try (InputStream inputStream = GameLoader.class.getClassLoader()
                .getResourceAsStream(RESOURCE_FILE)) {

            if (inputStream == null) {
                System.err.printf("❌ Файл '%s' не найден в resources!%n", RESOURCE_FILE);
                return Optional.empty();
            }

            GameData gameData = mapper.readValue(inputStream, GameData.class);
            return Optional.ofNullable(gameData);

        } catch (Exception e) {
            System.err.printf("❌ Ошибка при загрузке '%s': %s%n",
                    RESOURCE_FILE, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Проверяет существование сохранения
     */
    public boolean hasSavedGame() {
        return Files.exists(savePath);
    }

    /**
     * Возвращает путь к файлу сохранения
     */
    public Path getSavePath() {
        return savePath;
    }
}
