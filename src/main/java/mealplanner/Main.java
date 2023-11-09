package mealplanner;

import java.sql.SQLException;

public class Main {

  public static void main(String[] args) {
    try {
      MealPlanner planner = new MealPlanner();
      planner.start();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}