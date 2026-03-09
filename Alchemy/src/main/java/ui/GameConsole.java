package ui;

import services.GameService;
import services.CombineResult;
import services.CombineResultType;
import models.Element;
import models.Recipe;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GameConsole {
    private final GameService game;
    private final Scanner scanner;

    public GameConsole() {
        this.game = new GameService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printWelcomeMessage();

        while (true) {
            System.out.print("\n⚡ ");

            if (!scanner.hasNextLine()) {
                System.out.println("\n⚠ No input available. Exiting...");
                break;
            }

            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("q") || input.equals("quit") || input.equals("выход")) {
                printGoodbye();
                break;
            }

            if (input.isEmpty()) {
                continue;
            }

            processCommand(input);
        }

        scanner.close();
    }

    private void processCommand(String input) {
        switch (input) {
            case "i":
            case "inv":
            case "inventory":
            case "инвентарь":
                displayInventory();
                break;

            case "p":
            case "recipes":
            case "рецепты":
                displayAllRecipes();
                break;

            case "h":
            case "help":
            case "помощь":
                printHelp();
                break;

            default:
                handleCombineCommand(input);
                break;
        }
    }

    private void handleCombineCommand(String input) {
        String[] parts = input.split(" ");
        if (parts.length == 2) {
            CombineResult result = game.combine(parts[0], parts[1]);
            displayCombineResult(result);
        } else {
            System.out.println("❌ Неверная команда. Введите два элемента через пробел");
            System.out.println("   Или введите 'help' для списка команд");
        }
    }

    private void displayCombineResult(CombineResult result) {
        String emoji = switch (result.getType()) {
            case SUCCESS -> "🎉";
            case ALREADY_DISCOVERED -> "⚠";
            case MISSING_ELEMENTS -> "❌";
            case NOTHING -> "💨";
        };
        System.out.println(emoji + " " + result.getMessage());
    }

    private void displayInventory() {
        System.out.println("\n📦 ИНВЕНТАРЬ");
        System.out.println("═".repeat(40));

        Map<Integer, List<Element>> byLevel = game.getInventoryByLevel();

        for (Map.Entry<Integer, List<Element>> entry : byLevel.entrySet()) {
            System.out.printf("Уровень %d:\n", entry.getKey());
            for (Element e : entry.getValue()) {
                System.out.println("   • " + e.getName());
            }
        }
        System.out.println("═".repeat(40));
        System.out.printf("Всего: %d\n", game.getDiscoveredCount());
    }

    private void displayAllRecipes() {
        System.out.println("\n📜 ВСЕ РЕЦЕПТЫ");
        System.out.println("═".repeat(40));

        Map<Integer, List<Recipe>> recipesByLevel = game.getAllRecipesByLevel();

        for (Map.Entry<Integer, List<Recipe>> entry : recipesByLevel.entrySet()) {
            System.out.printf("Уровень %d:\n", entry.getKey());

            for (Recipe recipe : entry.getValue()) {
                boolean isDiscovered = game.isRecipeResultDiscovered(recipe);

                if (isDiscovered) {
                    System.out.printf("   • %s + %s = %s\n",
                            recipe.getFirst(), recipe.getSecond(), recipe.getResult());
                } else {
                    System.out.printf("   • ? + ? = ?\n");
                }
            }
        }
        System.out.println("═".repeat(40));
    }

    private void printWelcomeMessage() {
        System.out.println("\n" + "⭐".repeat(50));
        System.out.println("            АЛХИМИЯ - ИГРА В СОЗДАНИЕ ЭЛЕМЕНТОВ");
        System.out.println("⭐".repeat(50));
        System.out.println("\n🔬 Комбинируйте элементы чтобы открывать новые!");
        System.out.println("\n📝 Основные команды:");
        System.out.println("   • Элемент1 Элемент2 - попробовать комбинацию");
        System.out.println("   • i - инвентарь");
        System.out.println("   • p - все рецепты");
        System.out.println("   • h - помощь");
        System.out.println("   • q - выход");
        System.out.println("\n✨ Пример: Вода Огонь");
    }

    private void printHelp() {
        System.out.println("\n📖 ПОМОЩЬ");
        System.out.println("═".repeat(40));
        System.out.println("  i - инвентарь");
        System.out.println("  p - все рецепты");
        System.out.println("  h - помощь");
        System.out.println("  q - выход");
        System.out.println("\n  Элемент1 Элемент2 - комбинация");
        System.out.println("═".repeat(40));
    }

    private void printGoodbye() {
        System.out.println("\n" + "⭐".repeat(50));
        System.out.println("            СПАСИБО ЗА ИГРУ! ДО СВИДАНИЯ!");
        System.out.println("⭐".repeat(50));
    }
}
