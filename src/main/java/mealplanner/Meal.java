package mealplanner;

import java.util.List;
import java.util.stream.Collectors;

public final class Meal {

    private final int mealId;
    private final MealCategory category;
    private final String name;
    private final List<Ingredient> ingredients;

    public Meal(int mealId, MealCategory category, String name, List<Ingredient> ingredients) {
        this.mealId = mealId;
        this.category = category;
        this.name = name;
        this.ingredients = ingredients;
    }

    public Meal(MealCategory category, String name, List<Ingredient> ingredients) {
        this(-1, category, name, ingredients);
    }

    public int getMealId() {
        return mealId;
    }

    public MealCategory getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        String ingredientsString = ingredients.stream()
                .map(Ingredient::getIngredient)
                .collect(Collectors.joining("\n"));
        return String.format("\nName: %s\nIngredients:\n%s\n",
                name, ingredientsString);
    }
}
