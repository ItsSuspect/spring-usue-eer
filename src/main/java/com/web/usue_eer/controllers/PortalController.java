package com.web.usue_eer.controllers;

import com.web.usue_eer.entities.*;
import com.web.usue_eer.payload.request.DisciplineRequest;
import com.web.usue_eer.payload.request.TaskRequest;
import com.web.usue_eer.repository.DisciplineRepository;
import com.web.usue_eer.repository.UserRepository;
import com.web.usue_eer.security.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class PortalController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private DisciplineService disciplineService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserTaskService userTaskService;

    @GetMapping("/portal")
    public String index(Model model) {
        model.addAttribute("disciplines", getDisciplines());
        return "index";
    }

    @GetMapping("/portal/disciplines")
    public String disciplines(Model model) {
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/disciplines");
        return "index";
    }

    @GetMapping("/portal/disciplines/create")
    public String createDisciplines(Model model) {
        List<Group> groups = groupService.findAllGroups();
        List<User> users = userDetailsService.findAllUsers();

        model.addAttribute("groups", groups);
        model.addAttribute("users", users);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/create-discipline");
        return "index";
    }

    @PostMapping("/portal/disciplines/create-getUserGroup")
    public ResponseEntity<List<User>> getUsersGroup(@RequestBody Group group) {
        List<User> users = userDetailsService.findUsersByGroupName(group.getName());
        return ResponseEntity.ok(users);
    }

    @PostMapping("/portal/disciplines/create")
    public ResponseEntity<Discipline> createDiscipline(@Valid @RequestBody DisciplineRequest disciplineRequest) {
        String owner = SecurityContextHolder.getContext().getAuthentication().getName();
        Discipline discipline = disciplineService.saveDiscipline(new Discipline(disciplineRequest.getName(), owner));

        List<String> usernames = disciplineRequest.getUsers();

        for (String username : usernames) {
            User user = userDetailsService.findUserByUsername(username);
            if (user != null) {
                user.addDiscipline(discipline);
                userDetailsService.saveUser(user);
            }
        }

        User user = userDetailsService.findUserByUsername(owner);
        user.addDiscipline(discipline);
        userDetailsService.saveUser(user);
        return ResponseEntity.ok(discipline);
    }


    @GetMapping("/portal/discipline/{disciplineId}/{category}")
    public String getDisciplineCategoryContent(@PathVariable("disciplineId") Long disciplineId,
                                               @PathVariable("category") String category,
                                               Model model) {

        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("category", category);
        model.addAttribute("disciplineId", disciplineId);
        return "index";
    }

    @GetMapping("/portal/discipline/{disciplineId}/member-list")
    public String getMemberList(Model model, @PathVariable String disciplineId) {
        List<User> users = userDetailsService.findByDisciplinesId(Long.parseLong(disciplineId));
        Map<String, List<User>> usersByGroup = users.stream()
                .collect(Collectors.groupingBy(user -> user.getGroups().iterator().next().getName()));

        model.addAttribute("usersByGroup", usersByGroup);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/member-list");
        return "index";
    }

    @GetMapping("/portal/discipline/{disciplineId}/task-list")
    public String getTaskList(Model model, @PathVariable String disciplineId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);
        List<Task> tasks = taskService.findTasksByDisciplineId(Long.parseLong(disciplineId));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        for (Task task : tasks) {
            String formattedDateTimeIssue = task.getDateTimeIssue().format(formatter);
            task.setFormattedDateTimeIssue(formattedDateTimeIssue, formatter);

            String formattedDateTimeDelivery = task.getDateTimeDelivery().format(formatter);
            task.setFormattedDateTimeDelivery(formattedDateTimeDelivery, formatter);
        }


        model.addAttribute("tasks", tasks);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/task-list");
        return "index";
    }

    @GetMapping("/portal/discipline/{disciplineId}/task-list/{taskId}")
    public String getTask(Model model, @PathVariable String disciplineId, @PathVariable String taskId) {
        Task task = taskService.findTaskById(Long.parseLong(taskId));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        String formattedDateTimeIssue = task.getDateTimeIssue().format(formatter);
        task.setFormattedDateTimeIssue(formattedDateTimeIssue, formatter);

        String formattedDateTimeDelivery = task.getDateTimeDelivery().format(formatter);
        task.setFormattedDateTimeDelivery(formattedDateTimeDelivery, formatter);

        model.addAttribute("task", task);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/task");
        return "index";
    }

    @GetMapping("/portal/discipline/{disciplineId}/task-list/{taskId}/edit")
    public String getEditTask(Model model, @PathVariable String disciplineId, @PathVariable String taskId) {
        Task task = taskService.findTaskById(Long.parseLong(taskId));

        model.addAttribute("task", task);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/task");
        return "index";
    }

    @GetMapping("/portal/discipline/{disciplineId}/task-list/create")
    public String getTaskCreate(Model model, @PathVariable String disciplineId) {
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/create-task");
        return "index";
    }

    @PostMapping("/portal/discipline/{disciplineId}/task-list/create")
    public String createTask(@PathVariable String disciplineId, @RequestBody TaskRequest taskRequest) {
        LocalDateTime dateTimeIssue = LocalDateTime.of(taskRequest.getDateIssue(), taskRequest.getTimeIssue());
        LocalDateTime dateTimeDelivery = LocalDateTime.of(taskRequest.getDateDelivery(), taskRequest.getTimeDelivery());

        Discipline discipline = disciplineService.findDisciplineById(Long.parseLong(disciplineId));

        Task task = new Task(taskRequest.getName(), taskRequest.getMaxScore(), dateTimeIssue, dateTimeDelivery, taskRequest.getInstructionTask(), discipline);
        taskService.saveTask(task);
        return "index";
    }

    public List<Discipline> getDisciplines () {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        List<Discipline> disciplines = user.getDisciplines();
        Comparator<Discipline> comparator = Comparator.comparing(Discipline::getName);

        disciplines.sort(comparator);
        return disciplines;
    }
}