package models;

import java.util.List;
import java.util.Map;

public class GameData {
    private List<Element> elements;
    private Map<String, List<Recipe>> recipes;

    public List<Element> getElements() { return elements; }
    public void setElements(List<Element> elements) { this.elements = elements; }

    public Map<String, List<Recipe>> getRecipes() { return recipes; }
    public void setRecipes(Map<String, List<Recipe>> recipes) { this.recipes = recipes; }
}
