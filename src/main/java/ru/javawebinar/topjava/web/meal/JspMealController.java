package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController extends AbstractMealController {

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping("/meals")
    public String getAll(HttpServletRequest request, Model model) {
        String action = request.getParameter("action");
        if (action != null && action.equals("filter")) {
            model.addAttribute("meals", getBetween(
                    parseLocalDate(request.getParameter("startDate")),
                    parseLocalTime(request.getParameter("startTime")),
                    parseLocalDate(request.getParameter("endDate")),
                    parseLocalTime(request.getParameter("endTime"))
            ));
        } else {
            model.addAttribute("meals", getAll());
        }
        return "meals";
    }

    @PostMapping("/meals")
    public String createOrUpdate(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (!StringUtils.hasText(request.getParameter("id"))) {
            super.create(meal);
        } else {
            super.update(meal, getId(request));
        }
        return "redirect:/meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute(new Meal(null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0));
        return "mealForm";
    }

    @GetMapping("/update")
    public String update(Model model, HttpServletRequest request) {
        Meal meal = super.get(getId(request));
        model.addAttribute(meal);
        return "mealForm";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        super.delete(getId(request));
        return "redirect:meals";
    }
}
