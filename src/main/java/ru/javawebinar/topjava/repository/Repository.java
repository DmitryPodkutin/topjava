package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Repository {

    Meal save(Meal meal);

    Meal update(int id, Meal meal);

    Meal get(int id);

    void delete(int id);

    List<Meal> getAll();

}
