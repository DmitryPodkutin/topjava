package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealsTestData {
    public static final int MEAL_ID = START_SEQ + 2;
    public static final int MEAL_NOT_FOUND = START_SEQ + 10;

    public static final Meal USER_MEAL_1 = new Meal(100002, LocalDateTime.of(2020, 10, 18, 8, 15, 0), "Обед (User)", 860);
    public static final Meal USER_MEAL_2 = new Meal(100003, LocalDateTime.of(2020, 10, 19, 8, 14, 3), "Завтрак (User)", 250);
    public static final Meal USER_MEAL_3 = new Meal(100004, LocalDateTime.of(2020, 10, 19, 13, 0, 3), "Обед (User)", 700);
    public static final Meal USER_MEAL_4 = new Meal(100005, LocalDateTime.of(2020, 10, 19, 19, 0, 3), "Ужин (User)", 900);
    public static final Meal USER_MEAL_5 = new Meal(100006, LocalDateTime.of(2020, 10, 19, 9, 0, 0), "Завтрак (Admin)", 750);
    public static final Meal USER_MEAL_6 = new Meal(100007, LocalDateTime.of(2020, 10, 19, 20, 0, 0), "Ужин (Admin)", 250);

    public static final List<Meal> USER_MEALS = Arrays.asList(USER_MEAL_4, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);

    public static Meal getNewMeal() {
        return new Meal(LocalDateTime.of(2020, 10, 20, 8, 0, 0), "TEST CREATED", 100);
    }

    public static Meal getMealForUpdate() {
        return new Meal(MEAL_ID, LocalDateTime.of(2020, 10, 18, 8, 15, 0), "TEST UPDATE", 1);
    }
}

