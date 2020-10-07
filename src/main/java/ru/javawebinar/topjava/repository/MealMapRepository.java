package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealMapRepository implements Repository {
    Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();
    private static final AtomicInteger count = new AtomicInteger(0);

    @Override
    public Meal save(Meal meal) {
        meal.setId(count.getAndIncrement());
        return mealMap.put(meal.getId(), meal);
    }

    @Override
    public Meal update(int id, Meal meal) {
        if (mealMap.replace(meal.getId(), meal) == null) {
            return null;
        }
        return meal;
    }

    @Override
    public Meal get(int id) {
        return mealMap.get(id);
    }

    @Override
    public void delete(int id) {
        mealMap.remove(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealMap.values());
    }
}
