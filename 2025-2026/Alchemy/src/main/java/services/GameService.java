package services;

import models.GameData;
import models.Element;
import models.Recipe;

import java.util.*;

public class GameService {
    private final List<Element> discoveredElements;
    private final List<Recipe> allRecipes;
    private final Map<String, Element> elementMap;

    public GameService() {
        GameData data = JsonLoader.loadData();
        this.discoveredElements = new ArrayList<>(data.getElements());
        this.allRecipes = JsonLoader.flattenRecipes(data.getRecipes());

        this.elementMap = new HashMap<>();
        for (Element e : data.getElements()) {
            elementMap.put(e.getName().toLowerCase(), e);
        }
    }

    /**
     * Комбинирует два элемента и возвращает результат операции
     * @param element1 первый элемент
     * @param element2 второй элемент
     * @return объект Result с информацией о результате комбинации
     */
    public CombineResult combine(String element1, String element2) {
        String el1 = element1.trim().toLowerCase();
        String el2 = element2.trim().toLowerCase();

        if (!hasElement(el1) || !hasElement(el2)) {
            return new CombineResult(CombineResultType.MISSING_ELEMENTS,
                    "У вас нет одного из этих элементов!", null);
        }

        for (Recipe recipe : allRecipes) {
            String recipeFirst = recipe.getFirst().toLowerCase();
            String recipeSecond = recipe.getSecond().toLowerCase();

            if ((recipeFirst.equals(el1) && recipeSecond.equals(el2)) ||
                    (recipeFirst.equals(el2) && recipeSecond.equals(el1))) {

                if (hasElement(recipe.getResult().toLowerCase())) {
                    return new CombineResult(CombineResultType.ALREADY_DISCOVERED,
                            "Элемент '" + recipe.getResult() + "' уже открыт!", null);
                }

                Element newElement = new Element(
                        getNextElementId(),
                        recipe.getResult(),
                        recipe.getLevel()
                );

                discoveredElements.add(newElement);
                elementMap.put(newElement.getName().toLowerCase(), newElement);

                return new CombineResult(CombineResultType.SUCCESS,
                        "Поздравляем! Вы создали: " + newElement, newElement);
            }
        }

        return new CombineResult(CombineResultType.NOTHING,
                "Ничего не произошло...", null);
    }

    /**
     * Получить следующий доступный ID для элемента
     */
    private int getNextElementId() {
        return discoveredElements.stream()
                .mapToInt(Element::getId)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Возвращает инвентарь, сгруппированный по уровням
     * @return Map с уровнями и списками элементов
     */
    public Map<Integer, List<Element>> getInventoryByLevel() {
        Map<Integer, List<Element>> byLevel = new TreeMap<>();
        for (Element e : discoveredElements) {
            byLevel.computeIfAbsent(e.getLevel(), _ -> new ArrayList<>()).add(e);
        }
        return byLevel;
    }

    /**
     * Возвращает все рецепты, сгруппированные по уровням
     * @return Map с уровнями и списками рецептов
     */
    public Map<Integer, List<Recipe>> getAllRecipesByLevel() {
        Map<Integer, List<Recipe>> result = new TreeMap<>();
        for (Recipe recipe : allRecipes) {
            result.computeIfAbsent(recipe.getLevel(), _ -> new ArrayList<>()).add(recipe);
        }
        return result;
    }

    /**
     * Возвращает общее количество открытых элементов
     */
    public int getDiscoveredCount() {
        return discoveredElements.size();
    }

    /**
     * Проверяет наличие элемента
     */
    public boolean hasElement(String elementName) {
        if (elementName == null) return false;
        return elementMap.containsKey(elementName.toLowerCase().trim());
    }

    /**
     * Проверяет, открыт ли результат рецепта
     */
    public boolean isRecipeResultDiscovered(Recipe recipe) {
        return hasElement(recipe.getResult());
    }
}
