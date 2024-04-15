package com.web.usue_eer.controllers.UserCategory;

import com.web.usue_eer.entities.Task;
import com.web.usue_eer.entities.User;
import com.web.usue_eer.entities.UserDiscipline;
import com.web.usue_eer.payload.response.CalendarResponse;
import com.web.usue_eer.security.services.TaskService;
import com.web.usue_eer.security.services.UserDetailsServiceImpl;
import com.web.usue_eer.security.services.UserDisciplineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/portal")
public class CalendarController {
    private final UserDetailsServiceImpl userDetailsService;
    private final UserDisciplineService userDisciplineService;
    private final TaskService taskService;

    @Autowired
    public CalendarController(UserDetailsServiceImpl userDetailsService, UserDisciplineService userDisciplineService, TaskService taskService) {
        this.userDetailsService = userDetailsService;
        this.userDisciplineService = userDisciplineService;
        this.taskService = taskService;
    }

    @GetMapping("/calendar")
    public String getCalendar(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        List<UserDiscipline> userDisciplines = userDisciplineService.findUserDisciplinesByUserId(user.getId());
        List<CalendarResponse> calendarResponses = new ArrayList<>();

        for (UserDiscipline userDiscipline : userDisciplines) {
            List<Task> tasks = taskService.findTasksByDisciplineId(userDiscipline.getDiscipline().getId());
            for (Task task : tasks) {
                String date = task.getDateTimeDelivery().toLocalDate().format(DateTimeFormatter.ofPattern("d.M.yyyy"));
                String time = task.getDateTimeDelivery().toLocalTime().toString();
                calendarResponses.add(new CalendarResponse(
                        task.getDiscipline().getId(),
                        task.getId(),
                        task.getDiscipline().getName(),
                        task.getName(),
                        date,
                        time
                ));
            }
        }

        model.addAttribute("tasks", calendarResponses);
        model.addAttribute("content", "top-navigation/user-calendar/user-calendar");
        return "index";
    }
}
