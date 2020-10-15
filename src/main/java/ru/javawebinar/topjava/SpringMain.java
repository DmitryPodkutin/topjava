package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.create(new Meal(LocalDateTime.of(2020, 12, 22, 12, 30), "Meal Test", 2000));
            mealRestController.create(new Meal(LocalDateTime.of(2020, 12, 15, 12, 30), "Meal Test2", 1000));
            mealRestController.create(new Meal(LocalDateTime.of(2020, 12, 23, 12, 30), "Meal Test3", 1000));
            mealRestController.getAll(2000).forEach(System.out::println);
            System.out.println("-------------------");
            mealRestController.getAll(2000).stream().filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), LocalTime.of(7, 00), LocalTime.of(13, 30)))
                    .collect(Collectors.toList()).forEach(System.out::println);
            System.out.println("-------------------");
        }
    }
}
