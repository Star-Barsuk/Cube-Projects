package services;

import models.GameData;
import models.Element;
import models.Recipe;

import java.util.*;

/**
 * Сервисный класс, управляющий игровой логикой алхимии.
 */
public class GameService {
    private final List<Element> discoveredElements;
    private final List<Recipe> allRecipes;
    private final Map<String, Element> elementMap;
    private final Map<String, Recipe> recipeMap;

    /**
     * Конструктор сервиса игры.
     * Инициализирует списки элементов и рецептов, создает карты для быстрого доступа.
     *
     * @param gameData объект с начальными данными игры (элементы и рецепты)
     * @throws IllegalArgumentException если gameData или список элементов равны null
     */
    public GameService(GameData gameData) {
        if (gameData == null || gameData.getElements() == null) {
            throw new IllegalArgumentException("Некорректные игровые данные");
        }

        this.discoveredElements = new ArrayList<>(gameData.getElements());
        this.allRecipes = gameData.getRecipes() != null ? gameData.getRecipes() : new ArrayList<>();

        this.elementMap = new HashMap<>();
        for (Element e : gameData.getElements()) {
            elementMap.put(e.getName().toLowerCase(), e);
        }

        this.recipeMap = new HashMap<>();
        buildRecipeMap();
    }

    /**
     * Строит карту рецептов для быстрого поиска по комбинациям ингредиентов.
     * Ключом является отсортированный список ингредиентов, объединенный через разделитель.
     */
    private void buildRecipeMap() {
        recipeMap.clear();
        for (Recipe recipe : allRecipes) {
            if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
                List<String> sortedIngredients = new ArrayList<>(recipe.getIngredients());
                Collections.sort(sortedIngredients);
                String key = String.join("|", sortedIngredients);
                recipeMap.put(key.toLowerCase(), recipe);
            }
        }
    }

    /**
     * Комбинирует указанные элементы для создания нового.
     * Поддерживает комбинации из любого количества элементов (минимум 2).
     *
     * @param elementNames названия элементов для комбинации
     * @return CombineResponse с результатом комбинации
     */
    public CombineResponse combine(String... elementNames) {
        if (elementNames == null || elementNames.length < 2) {
            return new CombineResponse(CombineStatus.INVALID_INPUT, null);
        }

        List<String> normalizedNames = new ArrayList<>();
        for (String name : elementNames) {
            String normalized = name.trim().toLowerCase();
            if (!hasElement(normalized)) {
                return new CombineResponse(CombineStatus.MISSING_ELEMENTS, null);
            }
            normalizedNames.add(normalized);
        }

        Collections.sort(normalizedNames);
        String recipeKey = String.join("|", normalizedNames);
        Recipe matchedRecipe = recipeMap.get(recipeKey);

        if (matchedRecipe == null) {
            return new CombineResponse(CombineStatus.NOTHING, null);
        }

        Optional<Element> existingElement = getElement(matchedRecipe.getResult());
        if (existingElement.isPresent()) {
            return new CombineResponse(CombineStatus.ALREADY_DISCOVERED, existingElement.get());
        }

        Element newElement = new Element(
                getNextElementId(),
                matchedRecipe.getResult(),
                matchedRecipe.getLevel()
        );

        discoveredElements.add(newElement);
        elementMap.put(newElement.getName().toLowerCase(), newElement);

        return new CombineResponse(CombineStatus.SUCCESS, newElement);
    }

    /**
     * Комбинирует два элемента (упрощенный метод для обратной совместимости)
     *
     * @param elem1 первый элемент
     * @param elem2 второй элемент
     * @return результат комбинации или null
     */
    public Element combine(Element elem1, Element elem2) {
        if (elem1 == null || elem2 == null) {
            return null;
        }

        CombineResponse response = combine(elem1.getName(), elem2.getName());
        return response.getResult();
    }

    /**
     * Комбинирует три элемента
     *
     * @param elem1 первый элемент
     * @param elem2 второй элемент
     * @param elem3 третий элемент
     * @return результат комбинации или null
     */
    public Element combine(Element elem1, Element elem2, Element elem3) {
        if (elem1 == null || elem2 == null || elem3 == null) {
            return null;
        }

        CombineResponse response = combine(elem1.getName(), elem2.getName(), elem3.getName());
        return response.getResult();
    }

    /**
     * Возвращает все рецепты, сгруппированные по уровням.
     *
     * @return Map, где ключ - уровень рецепта, значение - список рецептов этого уровня
     */
    public Map<Integer, List<Recipe>> getAllRecipesByLevel() {
        Map<Integer, List<Recipe>> result = new TreeMap<>();
        for (Recipe recipe : allRecipes) {
            result.computeIfAbsent(recipe.getLevel(), _ -> new ArrayList<>()).add(recipe);
        }
        return result;
    }

    /**
     * Возвращает общее количество открытых элементов.
     *
     * @return int, количество открытых элементов
     */
    public int getDiscoveredCount() {
        return discoveredElements.size();
    }

    /**
     * Возвращает список открытых элементов (для сохранения).
     *
     * @return List, копия списка открытых элементов
     */
    public List<Element> getDiscoveredElements() {
        return new ArrayList<>(discoveredElements);
    }

    /**
     * Получает элемент по его имени.
     *
     * @param name имя элемента
     * @return Optional с элементом, если он найден, иначе пустой Optional
     */
    public Optional<Element> getElement(String name) {
        return Optional.ofNullable(elementMap.get(name.toLowerCase().trim()));
    }

    /**
     * Возвращает инвентарь, сгруппированный по уровням элементов.
     *
     * @return Map, где ключ - уровень элемента, значение - список элементов этого уровня
     */
    public Map<Integer, List<Element>> getInventoryByLevel() {
        Map<Integer, List<Element>> byLevel = new TreeMap<>();
        for (Element e : discoveredElements) {
            byLevel.computeIfAbsent(e.getLevel(), _ -> new ArrayList<>()).add(e);
        }
        return byLevel;
    }

    /**
     * Генерирует следующий уникальный идентификатор для нового элемента.
     *
     * @return следующий доступный ID
     */
    private int getNextElementId() {
        return discoveredElements.stream()
                .mapToInt(Element::getId)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Проверяет наличие элемента в инвентаре.
     *
     * @param elementName имя элемента для проверки
     * @return true если элемент есть в инвентаре, иначе false
     */
    public boolean hasElement(String elementName) {
        if (elementName == null) return false;
        return elementMap.containsKey(elementName.toLowerCase().trim());
    }

    /**
     * Проверяет наличие элемента в инвентаре по объекту Element.
     *
     * @param element элемент для проверки
     * @return true если элемент есть в инвентаре, иначе false
     */
    public boolean hasElement(Element element) {
        if (element == null) return false;
        return hasElement(element.getName());
    }

    /**
     * Проверяет, открыт ли результат указанного рецепта.
     *
     * @param recipe рецепт для проверки
     * @return true если результат рецепта уже открыт, иначе false
     */
    public boolean isRecipeResultDiscovered(Recipe recipe) {
        return hasElement(recipe.getResult());
    }

    /**
     * Получает все доступные рецепты (где все ингредиенты есть в инвентаре)
     *
     * @return список доступных рецептов
     */
    public List<Recipe> getAvailableRecipes() {
        List<Recipe> available = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            boolean allIngredientsAvailable = true;
            for (String ingredient : recipe.getIngredients()) {
                if (!hasElement(ingredient)) {
                    allIngredientsAvailable = false;
                    break;
                }
            }

            if (allIngredientsAvailable && !hasElement(recipe.getResult())) {
                available.add(recipe);
            }
        }

        return available;
    }

    /**
     * Получает рецепты, которые можно создать из двух элементов
     *
     * @param elem1 первый элемент
     * @param elem2 второй элемент
     * @return список возможных рецептов
     */
    public List<Recipe> getPossibleRecipes(Element elem1, Element elem2) {
        if (elem1 == null || elem2 == null) {
            return new ArrayList<>();
        }

        List<Recipe> possible = new ArrayList<>();
        Set<String> ingredientSet = new HashSet<>();
        ingredientSet.add(elem1.getName().toLowerCase());
        ingredientSet.add(elem2.getName().toLowerCase());

        for (Recipe recipe : allRecipes) {
            if (recipe.getIngredients().size() == 2) {
                Set<String> recipeSet = new HashSet<>();
                for (String ing : recipe.getIngredients()) {
                    recipeSet.add(ing.toLowerCase());
                }
                if (recipeSet.equals(ingredientSet)) {
                    possible.add(recipe);
                }
            }
        }

        return possible;
    }

    /**
     * Получает рецепты, которые можно создать из трех элементов
     *
     * @param elem1 первый элемент
     * @param elem2 второй элемент
     * @param elem3 третий элемент
     * @return список возможных рецептов
     */
    public List<Recipe> getPossibleRecipes(Element elem1, Element elem2, Element elem3) {
        if (elem1 == null || elem2 == null || elem3 == null) {
            return new ArrayList<>();
        }

        List<Recipe> possible = new ArrayList<>();
        Set<String> ingredientSet = new HashSet<>();
        ingredientSet.add(elem1.getName().toLowerCase());
        ingredientSet.add(elem2.getName().toLowerCase());
        ingredientSet.add(elem3.getName().toLowerCase());

        for (Recipe recipe : allRecipes) {
            if (recipe.getIngredients().size() == 3) {
                Set<String> recipeSet = new HashSet<>();
                for (String ing : recipe.getIngredients()) {
                    recipeSet.add(ing.toLowerCase());
                }
                if (recipeSet.equals(ingredientSet)) {
                    possible.add(recipe);
                }
            }
        }

        return possible;
    }

    /**
     * Добавляет новый элемент в инвентарь (для тестирования или читов)
     *
     * @param elementName имя элемента для добавления
     * @return true если элемент успешно добавлен, false если элемент уже существует
     */
    public boolean addElementToInventory(String elementName) {
        if (hasElement(elementName)) {
            return false;
        }

        // Ищем элемент в рецептах (чтобы получить уровень)
        for (Recipe recipe : allRecipes) {
            if (recipe.getResult().equalsIgnoreCase(elementName)) {
                Element newElement = new Element(
                        getNextElementId(),
                        recipe.getResult(),
                        recipe.getLevel()
                );
                discoveredElements.add(newElement);
                elementMap.put(newElement.getName().toLowerCase(), newElement);
                return true;
            }
        }

        return false;
    }
}