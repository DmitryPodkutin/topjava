package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        System.out.println(" --- Filter By Cycles --- ");
        filteredByCycles(meals, LocalTime.of(0, 0), LocalTime.of(23, 0), 2001).forEach(System.out::println);
        System.out.println(" --- Filter By Cycles Optional 2 --- ");
        filteredByCyclesOptional2(meals, LocalTime.of(0, 0), LocalTime.of(23, 0), 2001).forEach(System.out::println);
        System.out.println(" --- Filter By filteredByStreams --- ");
        filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 0), 2001).forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> totalCalories = new HashMap<>();
        List<UserMeal> filteredUserMeals = new ArrayList<>();
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            totalCalories.merge(userMeal.getDate(), userMeal.getCalories(), Integer::sum);
            if (isBetweenHalfOpen(startTime, endTime, userMeal)) {
                filteredUserMeals.add(userMeal);
            }
        }
        for (UserMeal userMeal : filteredUserMeals) {
            userMealWithExcesses.add(creatUserMealWithExcess(caloriesPerDay, totalCalories, userMeal));
        }
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByCyclesOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesCounterMap = new HashMap<>();
        Map<LocalDate, AtomicBoolean> excessCheckingMap = new HashMap<>();
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        for (UserMeal meal : meals) {
            final LocalDate mealDate = meal.getDate();
            caloriesCounterMap.merge(mealDate, meal.getCalories(), Integer::sum);
            AtomicBoolean isExceed = excessCheckingMap.computeIfAbsent(mealDate, date -> new AtomicBoolean());
            isExceed.set(caloriesCounterMap.get(mealDate) > caloriesPerDay);
            if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                userMealWithExcesses.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExceed));
            }
        }
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> totalCalories = meals.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate, Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(meal -> isBetweenHalfOpen(startTime, endTime, meal))
                .map(meal -> creatUserMealWithExcess(caloriesPerDay, totalCalories, meal))
                .collect(Collectors.toList());
    }

    private static boolean isBetweenHalfOpen(LocalTime startTime, LocalTime endTime, UserMeal userMeal) {
        return TimeUtil.isBetweenHalfOpen(userMeal.getTime(), startTime, endTime);
    }

    private static UserMealWithExcess creatUserMealWithExcess(int caloriesPerDay, Map<LocalDate, Integer> totalCalories, UserMeal userMeal) {
        return new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), caloriesPerDay < totalCalories.get(userMeal.getDate()));
    }
}