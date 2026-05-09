package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.*;

public class Recipe {
    private List<String> ingredients;
    private String result;
    private int level;

    public Recipe() {
        this.ingredients = new ArrayList<>();
    }

    public Recipe(List<String> ingredients, String result, int level) {
        this.ingredients = new ArrayList<>(ingredients);
        this.result = result;
        this.level = level;
        Collections.sort(this.ingredients);
    }

    public List<String> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
        if (this.ingredients != null) {
            Collections.sort(this.ingredients);
        }
    }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    /**
     * Проверяет, подходит ли набор ингредиентов для этого рецепта
     */
    public boolean matches(List<String> inputIngredients) {
        if (ingredients == null || inputIngredients == null) return false;
        if (ingredients.size() != inputIngredients.size()) return false;

        List<String> sortedInput = new ArrayList<>(inputIngredients);
        Collections.sort(sortedInput);
        List<String> sortedRecipe = new ArrayList<>(ingredients);
        Collections.sort(sortedRecipe);

        return sortedInput.equals(sortedRecipe);
    }

    @Override
    public String toString() {
        return String.join(" + ", ingredients) + " = " + result + " (ур." + level + ")";
    }
}
