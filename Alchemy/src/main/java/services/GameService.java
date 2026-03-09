package services;

import models.GameData;
import models.Element;
import models.Recipe;

import java.util.*;

public class GameService {
    private List<Element> discoveredElements;
    private List<Recipe> allRecipes;
    private Map<String, Element> elementMap;
    private Map<Integer, List<Recipe>> recipesByLevel;

    public GameService() {
        GameData data = JsonLoader.loadData();
        this.discoveredElements = new ArrayList<>(data.getElements());
        this.allRecipes = JsonLoader.flattenRecipes(data.getRecipes());
        this.recipesByLevel = new TreeMap<>();

        for (Map.Entry<String, List<Recipe>> entry : data.getRecipes().entrySet()) {
            int level = Integer.parseInt(entry.getKey());
            recipesByLevel.put(level, entry.getValue());
        }

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
                        discoveredElements.size() + 1,
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

    public boolean hasElement(String elementName) {
        return elementMap.containsKey(elementName.toLowerCase());
    }

    /**
     * Возвращает инвентарь, сгруппированный по уровням
     * @return Map с уровнями и списками элементов
     */
    public Map<Integer, List<Element>> getInventoryByLevel() {
        Map<Integer, List<Element>> byLevel = new TreeMap<>();
        for (Element e : discoveredElements) {
            byLevel.computeIfAbsent(e.getLevel(), k -> new ArrayList<>()).add(e);
        }
        return byLevel;
    }

    /**
     * Возвращает общее количество открытых элементов
     */
    public int getDiscoveredCount() {
        return discoveredElements.size();
    }

    /**
     * Возвращает все рецепты, сгруппированные по уровням
     * @return Map с уровнями и списками рецептов
     */
    public Map<Integer, List<Recipe>> getAllRecipesByLevel() {
        return recipesByLevel;
    }

    /**
     * Проверяет, открыт ли результат рецепта
     */
    public boolean isRecipeResultDiscovered(Recipe recipe) {
        return hasElement(recipe.getResult());
    }
}
