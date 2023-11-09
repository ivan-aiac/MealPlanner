package mealplanner;

import java.util.HashMap;
import java.util.Map;

import static mealplanner.MealCategory.*;

public class Plan {
    private final WeekDay day;
    private final Map<MealCategory, Meal> meals;

    public Plan(WeekDay day) {
        meals = new HashMap<>(3);
        this.day = day;
    }

    public void addMeal(MealCategory category, Meal meal) {
        meals.putIfAbsent(category, meal);
    }

    public WeekDay getDay() {
        return day;
    }

    public Meal getPlannedMealFor(MealCategory category) {
        return meals.get(category);
    }

    @Override
    public String toString() {
        return String.format("\n%s\nBreakfast: %s\nLunch: %s\nDinner: %s\n"
                , day, meals.get(BREAKFAST).getName(), meals.get(LUNCH).getName(), meals.get(DINNER).getName());
    }
}
