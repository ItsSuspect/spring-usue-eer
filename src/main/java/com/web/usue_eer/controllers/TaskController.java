package com.web.usue_eer.controllers;

import com.web.usue_eer.entities.*;
import com.web.usue_eer.payload.request.TaskRequest;
import com.web.usue_eer.security.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/portal/discipline")
public class TaskController {
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

    @GetMapping("/{disciplineId}/task-list/create")
    public String getTaskCreate(Model model, @PathVariable String disciplineId) {
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/create-task");
        return "index";
    }

    @GetMapping("/{disciplineId}/task-list")
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

    @GetMapping("/{disciplineId}/task-list/{taskId}")
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

    @PostMapping("/{disciplineId}/task-list/create")
    public ResponseEntity<Void> createTask(@PathVariable String disciplineId, @RequestBody TaskRequest taskRequest) {
        LocalDateTime dateTimeIssue = LocalDateTime.of(taskRequest.getDateIssue(), taskRequest.getTimeIssue());
        LocalDateTime dateTimeDelivery = LocalDateTime.of(taskRequest.getDateDelivery(), taskRequest.getTimeDelivery());

        Discipline discipline = disciplineService.findDisciplineById(Long.parseLong(disciplineId));

        Task task = new Task(taskRequest.getName(), taskRequest.getMaxScore(), dateTimeIssue, dateTimeDelivery, taskRequest.getInstructionTask(), discipline);
        taskService.saveTask(task);
        return ResponseEntity.ok().build();
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
