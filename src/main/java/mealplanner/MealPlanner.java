package mealplanner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static mealplanner.MealCategory.*;

public class MealPlanner {

    private final MealDB mealDB;
    private final Scanner scanner;
    private boolean running;
    private static final String INGREDIENTS_REGEX = "^[a-zA-Z][a-zA-Z ]*(?:, ?[a-zA-Z][a-zA-Z ]*)*$";
    private static final String MEAL_NAME_REGEX = "^[a-zA-Z][a-zA-Z ]*$";

    public MealPlanner() throws SQLException {
        mealDB = new MealDB();
        scanner = new Scanner(System.in);
    }

    public void start() {
        running = true;
        while (running) {
            System.out.println("What would you like to do (add, show, plan, save, exit)?");
            String command = scanner.nextLine();
            switch (command) {
                case "add" -> addMeal();
                case "show" -> showMeals();
                case "plan" -> weeklyPlan();
                case "save" -> saveShoppingList();
                case "exit" -> exit();
            }
        }
    }

    private void addMeal() {
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        MealCategory category = requestMealCategory();
        String name = null;
        System.out.println("Input the meal's name:");
        while (name == null) {
            String input = scanner.nextLine();
            if (input.matches(MEAL_NAME_REGEX)) {
                name = input;
            } else {
                System.out.println("Wrong format. Use letters only!");
            }
        }
        String ingredients = null;
        System.out.println("Input the ingredients:");
        while (ingredients == null) {
            String input = scanner.nextLine();
            if (input.matches(INGREDIENTS_REGEX)) {
                ingredients = input;
            } else {
                System.out.println("Wrong format. Use letters only!");
            }
        }
        List<Ingredient> ingredientList;
        ingredientList = Arrays.stream(ingredients.split(","))
                .map(String::trim)
                .map(Ingredient::new)
                .toList();
        mealDB.addMeal(new Meal(category,name,ingredientList));
        System.out.println("The meal has been added!");
    }

    private MealCategory requestMealCategory() {
        Optional<MealCategory> category;
        do {
            category = MealCategory.findByName(scanner.nextLine());
            if (category.isEmpty()) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            }
        } while(category.isEmpty());
        return category.get();
    }

    private void showMeals(){
        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        MealCategory category = requestMealCategory();
        List<Meal> meals = mealDB.findMealsByCategory(category, false);
        if (meals.isEmpty()) {
            System.out.println("No meals found.");
        } else {
            System.out.println("Category: " + category);
            meals.forEach(System.out::print);
        }
    }

    private void weeklyPlan() {
        Map<MealCategory, List<Meal>> mealsMap = new HashMap<>(3);
        mealsMap.put(BREAKFAST, mealDB.findMealsByCategory(BREAKFAST,true));
        mealsMap.put(LUNCH, mealDB.findMealsByCategory(MealCategory.LUNCH, true));
        mealsMap.put(DINNER, mealDB.findMealsByCategory(MealCategory.DINNER, true));
        List<Plan> weeklyPlan = new ArrayList<>(7);
        for (WeekDay day : WeekDay.values()) {
            System.out.println(day);
            Plan dailyPlan = new Plan(day);
            for (MealCategory category : MealCategory.values()) {
                List<Meal> mealList = mealsMap.get(category);
                mealList.forEach(m -> System.out.println(m.getName()));
                System.out.printf("Choose the %s for %s from the list above:%n", category, day);
                Optional<Meal> meal;
                do {
                    String input = scanner.nextLine();
                    meal = mealList.stream()
                            .filter(m -> m.getName().equals(input))
                            .findFirst();
                    if (meal.isEmpty()) {
                        System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
                    }
                } while (meal.isEmpty());
                dailyPlan.addMeal(category, meal.get());
            }
            weeklyPlan.add(dailyPlan);
            System.out.printf("Yeah! We planned the meals for %s.%n", day);
        }
        weeklyPlan.forEach(System.out::print);
        mealDB.addWeeklyPlan(weeklyPlan);
    }

    private void saveShoppingList() {
        if (mealDB.isWeeklyPlanAvailable()) {
            List<Ingredient> ingredients = mealDB.findWeeklyPlanIngredientsList();
            System.out.println("Input a filename:");
            String fileName = scanner.nextLine();
            String fileData = ingredients.stream()
                    .map(Ingredient::toString)
                    .collect(Collectors.joining(System.lineSeparator()));
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                writer.write(fileData);
                writer.close();
                System.out.println("Saved!");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Unable to save. Plan your meals first.");
        }
    }

    private void exit() {
        System.out.println("Bye!");
        running = false;
    }
}
