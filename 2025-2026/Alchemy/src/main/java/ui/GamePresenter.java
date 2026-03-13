package ui;

import services.GameService;
import services.CombineResponse;
import models.Element;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GamePresenter {
    private final GameService gameService;

    public GamePresenter(GameService gameService) {
        this.gameService = gameService;
    }

    public String combine(String... elementNames) {
        CombineResponse response = gameService.combine(elementNames);

        String elements = Arrays.stream(elementNames)
                .map(String::trim)
                .collect(Collectors.joining(" + "));

        return switch (response.getStatus()) {
            case SUCCESS -> "🎉 Поздравляем! " + elements + " = " + response.getElement();
            case ALREADY_DISCOVERED -> "⚠ Элемент '" + response.getElement().getName() + "' уже открыт!";
            case MISSING_ELEMENTS -> "❌ У вас нет одного из этих элементов!";
            case INVALID_INPUT -> "❌ Нужно указать минимум 2 элемента для комбинации!";
            case NOTHING -> "💨 " + elements + " = ничего не произошло...";
        };
    }

    public String formatInventory() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n📦 ИНВЕНТАРЬ\n");
        sb.append("═".repeat(40)).append("\n");

        var byLevel = gameService.getInventoryByLevel();

        for (var entry : byLevel.entrySet()) {
            sb.append(String.format("Уровень %d:\n", entry.getKey()));
            for (Element e : entry.getValue()) {
                sb.append("   • ").append(e.getName()).append("\n");
            }
        }
        sb.append("═".repeat(40)).append("\n");
        sb.append(String.format("Всего: %d\n", gameService.getDiscoveredCount()));

        return sb.toString();
    }

    public String formatAllRecipes() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n📜 ВСЕ РЕЦЕПТЫ\n");
        sb.append("═".repeat(40)).append("\n");

        var recipesByLevel = gameService.getAllRecipesByLevel();

        for (var entry : recipesByLevel.entrySet()) {
            sb.append(String.format("\n🌟 УРОВЕНЬ %d:\n", entry.getKey()));
            sb.append("─".repeat(30)).append("\n");

            for (var recipe : entry.getValue()) {
                boolean isDiscovered = gameService.isRecipeResultDiscovered(recipe);

                if (isDiscovered) {
                    sb.append("  ✅ ")
                            .append(String.join(" + ", recipe.getIngredients()))
                            .append(" = ")
                            .append(recipe.getResult())
                            .append("\n");
                } else {
                    for (int i = 0; i < recipe.getIngredients().size(); i++) {
                        if (i > 0) sb.append(" + ");
                        sb.append("?");
                    }
                    sb.append(" = ?\n");
                }
            }
        }
        sb.append("\n").append("═".repeat(40)).append("\n");
        sb.append(String.format("📊 Всего рецептов: %d\n",
                recipesByLevel.values().stream().mapToInt(List::size).sum()));

        return sb.toString();
    }

    public String formatHelp() {
        return """
                
                📖 ПОМОЩЬ
                ════════════════════════════════════════
                  i - инвентарь
                  p - все рецепты
                  h - помощь
                  q - выход
                
                  Элемент1 Элемент2 ... ЭлементN - комбинация
                  (минимум 2 элемента, можно больше)
                ════════════════════════════════════════""";
    }

    public String formatWelcome() {
        return """
                
                ⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐
                            АЛХИМИЯ - ИГРА В СОЗДАНИЕ ЭЛЕМЕНТОВ
                ⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐
                
                🔬 Комбинируйте элементы чтобы открывать новые!
                
                📝 Основные команды:
                   • Элемент1 Элемент2 ... ЭлементN - попробовать комбинацию
                   • i - инвентарь
                   • p - все рецепты
                   • h - помощь
                   • q - выход
                
                ✨ Пример: Вода Огонь""";
    }

    public String formatGoodbye() {
        return """
                
                ⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐
                            СПАСИБО ЗА ИГРУ! ДО СВИДАНИЯ!
                ⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐""";
    }
}
