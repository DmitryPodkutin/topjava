package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealsTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;


@ContextConfiguration("classpath:spring/spring-app.xml")
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))

public class MealServiceTest {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;


    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID, USER_ID);
        assertThat(USER_MEAL_1).usingRecursiveComparison().isEqualTo(meal);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotOwen() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, USER_ID));
    }

    @Test
    public void deleteNoteFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNoteOwen() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> expectedMeals = service.getBetweenInclusive(LocalDate.of(2020, 10, 17), LocalDate.of(2020, 10, 18), USER_ID);
        List<Meal> actualMeals = Collections.singletonList(USER_MEAL_1);
        assertThat(actualMeals).usingRecursiveComparison().isEqualTo(expectedMeals);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertThat(USER_MEALS).usingRecursiveComparison().isEqualTo(meals);
    }

    @Test
    public void update() {
        Meal updated = getMealForUpdate();
        service.update(updated, USER_ID);
        assertThat(service.get(MEAL_ID, USER_ID)).usingRecursiveComparison().isEqualTo(updated);
    }

    @Test
    public void updateNoteOwen() {
        assertThrows(NotFoundException.class, () -> service.update(getMealForUpdate(), ADMIN_ID));
    }

    @Test
    public void create() {
        Meal newMeal = getNewMeal();
        Meal created = service.create(newMeal, ADMIN_ID);
        Meal expected = getNewMeal();
        expected.setId(created.getId());
        assertThat(created).usingRecursiveComparison().isEqualTo(expected);
        assertThat(service.get(created.getId(), ADMIN_ID)).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DuplicateKeyException.class, () ->
                service.create(new Meal(LocalDateTime.of(2020, 10, 19, 9, 0, 0), "Duplicate DateTime", 1), ADMIN_ID));
    }
}