package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealMapRepository;
import ru.javawebinar.topjava.repository.Repository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String INSERT_OR_EDIT = "/edit.jsp";
    private static final String LIST = "/meals.jsp";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final Repository repository = new MealMapRepository();

    public void init() {
        repository.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        repository.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        repository.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        repository.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        repository.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        repository.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        repository.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action = request.getParameter("action");
        if (action == null){
            action ="";
        }
        switch (action) {
            case ("save"):
                log.debug("Save Meal");
                forward = INSERT_OR_EDIT;
                break;
            case ("edit"):
                forward = INSERT_OR_EDIT;
                Meal meal = repository.get(Integer.parseInt(request.getParameter("id")));
                request.setAttribute("meal", meal);
                log.debug("Edit Meal");
                break;
            case ("delete"):
                repository.delete(Integer.parseInt(request.getParameter("id")));
                response.sendRedirect("meals");
                log.debug("Delete Meal");
                return;
            default:
                forward = LIST;
                request.setAttribute("meals", getMealTo());
                log.debug("Redirect to Meals List");
                break;
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String id = request.getParameter("id");
        String date = request.getParameter("date");
        Meal meal = new Meal(getLocalDateTime(request, date), description, calories);
        if (id == null || id.isEmpty()) {
            repository.save(meal);
        } else {
            final int parseInt = Integer.parseInt(request.getParameter("id"));
            meal.setId(parseInt);
            repository.update(parseInt, meal);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST);
        request.setAttribute("meals", getMealTo());
        view.forward(request, response);
    }

    private LocalDateTime getLocalDateTime(HttpServletRequest request, String date) {
        LocalDateTime localDateTime;
        if (date.isEmpty()) {
            localDateTime = LocalDateTime.now();
        } else {
            localDateTime = LocalDateTime.parse(request.getParameter("date"), dateTimeFormatter);
        }
        return localDateTime;
    }

    private List<MealTo> getMealTo() {
        return filteredByStreams(repository.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
    }
}
