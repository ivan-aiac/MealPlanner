package mealplanner;

import java.util.Arrays;
import java.util.Optional;

public enum MealCategory {
    BREAKFAST, LUNCH, DINNER;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static Optional<MealCategory> findByName(String name) {
        return Arrays.stream(MealCategory.values())
                .filter(c -> c.name().equalsIgnoreCase(name))
                .findFirst();
    }
}
