package mealplanner;

public class Ingredient {
    private final String ingredient;
    private int count;

    public Ingredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public Ingredient(String ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    public String getIngredient() {
        return ingredient;
    }

    @Override
    public String toString() {
        return count == 1 ? ingredient
                : String.format(String.format("%s x%d", ingredient, count));
    }
}
