package ui;

import services.GameService;

import java.util.*;

public class GameConsole {
    private static final Set<String> EXIT_COMMANDS = Set.of("q", "quit", "выход");
    private static final Set<String> INVENTORY_COMMANDS = Set.of("i", "inv", "inventory", "инвентарь");
    private static final Set<String> RECIPES_COMMANDS = Set.of("p", "recipes", "рецепты");
    private static final Set<String> HELP_COMMANDS = Set.of("h", "help", "помощь");

    private final GamePresenter presenter;
    private final Scanner scanner;

    public GameConsole(GameService gameService) {
        this.presenter = new GamePresenter(gameService);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println(presenter.formatWelcome());

        while (true) {
            System.out.print("\n⚡ ");

            if (!scanner.hasNextLine()) {
                System.out.println("\n⚠ Нет ввода. Завершение...");
                break;
            }

            String input = scanner.nextLine().trim().toLowerCase();

            if (EXIT_COMMANDS.contains(input)) {
                System.out.println(presenter.formatGoodbye());
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
        if (INVENTORY_COMMANDS.contains(input)) {
            System.out.println(presenter.formatInventory());
        } else if (RECIPES_COMMANDS.contains(input)) {
            System.out.println(presenter.formatAllRecipes());
        } else if (HELP_COMMANDS.contains(input)) {
            System.out.println(presenter.formatHelp());
        } else {
            handleCombineCommand(input);
        }
    }

    private void handleCombineCommand(String input) {
        String[] parts = input.split("\\s+");
        if (parts.length >= 2) {
            String result = presenter.combine(parts);
            System.out.println(result);
        } else {
            System.out.println("❌ Неверная команда. Введите минимум два элемента через пробел");
            System.out.println("   Или введите 'help' для списка команд");
        }
    }
}
