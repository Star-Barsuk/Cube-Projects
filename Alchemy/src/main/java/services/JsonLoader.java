package services;

import models.GameData;
import models.Recipe;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.*;

public class JsonLoader {

    public static GameData loadData() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream inputStream = JsonLoader.class.getClassLoader()
                    .getResourceAsStream("elements.json");
            if (inputStream == null) {
                throw new RuntimeException("Файл elements.json не найден!");
            }
            return mapper.readValue(inputStream, GameData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new GameData();
        }
    }

    public static List<Recipe> flattenRecipes(Map<String, List<Recipe>> recipesByLevel) {
        List<Recipe> allRecipes = new ArrayList<>();
        for (Map.Entry<String, List<Recipe>> entry : recipesByLevel.entrySet()) {
            int level = Integer.parseInt(entry.getKey());
            for (Recipe recipe : entry.getValue()) {
                recipe.setLevel(level);
                allRecipes.add(recipe);
            }
        }
        return allRecipes;
    }
}
