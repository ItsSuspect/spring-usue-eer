package com.web.usue_eer.controllers;

import com.web.usue_eer.entities.*;
import com.web.usue_eer.payload.request.SendTaskRequest;
import com.web.usue_eer.payload.request.SendTaskTeacherRequest;
import com.web.usue_eer.payload.request.TaskRequest;
import com.web.usue_eer.payload.response.TaskResponse;
import com.web.usue_eer.security.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private UserDisciplineService userDisciplineService;

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
        List<UserTask> userTasks = userTaskService.findUserTasksByUserId(user.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(Long.parseLong(disciplineId), user.getId());
        boolean authorities = userDiscipline.getAccessType().name().equals("LEADER");

        List<TaskResponse> taskResponses = tasks.stream()
                .map(task -> {
                    TaskResponse taskResponse = new TaskResponse();

                    taskResponse.setId(task.getId());
                    taskResponse.setInstruction(task.getInstruction());
                    taskResponse.setName(task.getName());
                    taskResponse.setMaxScore(task.getMaxScore());
                    taskResponse.setFormattedDateTimeIssue(task.getDateTimeIssue().format(formatter));
                    taskResponse.setFormattedDateTimeDelivery(task.getDateTimeDelivery().format(formatter));

                    for (UserTask userTask : userTasks) {
                        if (userTask.getTask().getId().equals(task.getId())) {
                            taskResponse.setStatus(userTask.getStatus());
                            taskResponse.setCommentStudent(userTask.getCommentStudent());
                            taskResponse.setDateDelivery(userTask.getDateDelivery().format(formatter));
                            taskResponse.setCommentTeacher(userTask.getCommentTeacher());
                            taskResponse.setResultScore(userTask.getResultScore());
                            taskResponse.setSending(true);
                            break;
                        }
                    }
                    if (taskResponse.getStatus() == null) taskResponse.setStatus(task.getStatus());
                    if (!taskResponse.isSending()) taskResponse.setSending(false);
                    if (authorities) {
                        taskResponse.setCountSend(userTaskService.countUserTasksByDisciplineIdAndTaskId(Long.parseLong(disciplineId), task.getId()));
                        taskResponse.setCountChecked(userTaskService.countByStatusAndTaskId(task.getId()));
                    }
                    taskResponse.setUser(user);
                    return taskResponse;
                }).collect(Collectors.toList());

        model.addAttribute("authorities", authorities);
        model.addAttribute("tasks", taskResponses);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/task-list");
        return "index";
    }

    @GetMapping("/{disciplineId}/task-list/{taskId}/completed-tasks")
    public String getSendingTasks(Model model, @PathVariable String disciplineId, @PathVariable String taskId) {
        Task task = taskService.findTaskById(Long.parseLong(taskId));
        List<UserTask> userTasks = userTaskService.findUserTasksByTaskId(Long.parseLong(taskId));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        List<TaskResponse> taskResponses = userTasks.stream()
                .map(userTask -> {
                    TaskResponse taskResponse = new TaskResponse();
                    taskResponse.setId(Long.parseLong(taskId));
                    taskResponse.setUser(userTask.getUser());
                    taskResponse.setStatus(userTask.getStatus());
                    taskResponse.setResultScore(userTask.getResultScore());
                    taskResponse.setMaxScore(task.getMaxScore());
                    taskResponse.setDateDelivery(userTask.getDateDelivery().format(formatter));
                    return taskResponse;
                }).collect(Collectors.toList());

        model.addAttribute("tasks", taskResponses);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/completed-tasks");
        return "index";
    }

    @GetMapping("/{disciplineId}/task-list/{taskId}/{username}/task-check")
    public String getSendTask(Model model, @PathVariable String disciplineId, @PathVariable String taskId, @PathVariable String username) {
        User user = userDetailsService.findUserByUsername(username);
        Task task = taskService.findTaskById(Long.parseLong(taskId));
        UserTask userTask = userTaskService.findUserTaskByUserIdAndTaskId(user.getId(), Long.parseLong(taskId)).get();

        TaskResponse taskResponse;
        taskResponse = new TaskResponse();
        taskResponse.setId(Long.parseLong(taskId));
        taskResponse.setName(task.getName());
        taskResponse.setMaxScore(task.getMaxScore());
        taskResponse.setCommentStudent(userTask.getCommentStudent());
        taskResponse.setCommentTeacher(userTask.getCommentTeacher());
        taskResponse.setResultScore(userTask.getResultScore());
        taskResponse.setInstruction(task.getInstruction());
        taskResponse.setUser(user);


        model.addAttribute("task", taskResponse);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/task-check");
        return "index";
    }

    @GetMapping("/{disciplineId}/task-list/{taskId}")
    public String getTask(Model model, @PathVariable String disciplineId, @PathVariable String taskId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(Long.parseLong(disciplineId), user.getId());
        boolean authorities = userDiscipline.getAccessType().name().equals("LEADER");

        Optional<UserTask> optionalUserTask = userTaskService.findUserTaskByUserIdAndTaskId(user.getId(), Long.parseLong(taskId));
        Task task = taskService.findTaskById(Long.parseLong(taskId));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        task.setFormattedDateTimeIssue(task.getDateTimeIssue().format(formatter));
        task.setFormattedDateTimeDelivery(task.getDateTimeDelivery().format(formatter));
        TaskResponse taskResponse;
        if (optionalUserTask.isPresent()) {
            UserTask userTask = optionalUserTask.get();

            taskResponse = new TaskResponse(
                    Long.parseLong(taskId),
                    task.getInstruction(),
                    task.getName(),
                    task.getMaxScore(),
                    task.getFormattedDateTimeDelivery(),
                    task.getFormattedDateTimeIssue(),
                    userTask.getDateDelivery().format(formatter),
                    userTask.getStatus(),
                    userTask.getCommentStudent(),
                    userTask.getCommentTeacher(),
                    userTask.getResultScore(),
                    user,
                    true
            );
        } else {
            taskResponse = new TaskResponse(
                    Long.parseLong(taskId),
                    task.getInstruction(),
                    task.getName(),
                    task.getMaxScore(),
                    task.getFormattedDateTimeDelivery(),
                    task.getFormattedDateTimeIssue(),
                    user,
                    false
            );
        }

        model.addAttribute("task", taskResponse);
        model.addAttribute("authorities", authorities);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/task");
        return "index";
    }

    @PostMapping("/{disciplineId}/task-list/{taskId}/send")
    public ResponseEntity<Void> sendingTask(Model model, @PathVariable String disciplineId, @PathVariable String taskId, @RequestBody SendTaskRequest sendTaskRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        Task task = taskService.findTaskById(Long.parseLong(taskId));
        LocalDateTime localDateTime = LocalDateTime.now().withSecond(0).withNano(0);

        UserTask userTask = new UserTask(user, task, "Сдано", localDateTime, sendTaskRequest.getComment());
        userTaskService.saveUserTask(userTask);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{disciplineId}/task-list/{taskId}/edit")
    public String getEditTask(Model model, @PathVariable String disciplineId, @PathVariable String taskId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(Long.parseLong(disciplineId), user.getId());
        boolean authorities = userDiscipline.getAccessType().name().equals("LEADER");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        Task task = taskService.findTaskById(Long.parseLong(taskId));
        task.setFormattedDateTimeDelivery(task.getDateTimeDelivery().format(formatter));
        task.setFormattedDateTimeIssue(task.getDateTimeIssue().format(formatter));

        LocalDateTime localDateTimeIssue = task.getDateTimeIssue();
        Date dateIssue = Date.from(localDateTimeIssue.atZone(ZoneId.systemDefault()).toInstant());
        model.addAttribute("formattedDateIssue", dateIssue);

        LocalDateTime localDateTimeDelivery = task.getDateTimeIssue();
        Date dateDelivery = Date.from(localDateTimeDelivery.atZone(ZoneId.systemDefault()).toInstant());
        model.addAttribute("formattedDateIssue", dateDelivery);

        model.addAttribute("task", task);
        model.addAttribute("authorities", authorities);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/edit-task");
        return "index";
    }

    @PostMapping("/{disciplineId}/task-list/{taskId}/edit")
    public ResponseEntity<Void> postEditTask(Model model, @PathVariable String disciplineId, @PathVariable String taskId, @RequestBody TaskRequest taskRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        LocalDateTime dateTimeIssue = LocalDateTime.of(taskRequest.getDateIssue(), taskRequest.getTimeIssue());
        LocalDateTime dateTimeDelivery = LocalDateTime.of(taskRequest.getDateDelivery(), taskRequest.getTimeDelivery());

        Task task = taskService.findTaskById(Long.parseLong(taskId));
        task.setName(taskRequest.getName());
        task.setMaxScore(taskRequest.getMaxScore());
        task.setInstruction(taskRequest.getInstructionTask());
        task.setDateTimeDelivery(dateTimeDelivery);
        task.setDateTimeIssue(dateTimeIssue);

        taskService.saveTask(task);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{disciplineId}/task-list/{taskId}/answer-teacher")
    public ResponseEntity<Void> answerTask(Model model, @PathVariable String disciplineId, @PathVariable String taskId, @RequestBody SendTaskTeacherRequest sendTaskTeacherRequest) {
        User user = userDetailsService.findUserByUsername(sendTaskTeacherRequest.getUsername());

        Optional<UserTask> optionalUserTask = userTaskService.findUserTaskByUserIdAndTaskId(user.getId(), Long.parseLong(taskId));
        System.out.println(optionalUserTask.isPresent());
        if (optionalUserTask.isPresent()) {
            UserTask userTask = optionalUserTask.get();
            userTask.setCommentTeacher(sendTaskTeacherRequest.getCommentTeacher());
            userTask.setResultScore(sendTaskTeacherRequest.getResultScore());
            userTask.setStatus("Проверено");
            userTaskService.saveUserTask(userTask);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{disciplineId}/task-list/create")
    public ResponseEntity<Void> createTask(@PathVariable String disciplineId, @RequestBody TaskRequest taskRequest) {
        LocalDateTime dateTimeIssue = LocalDateTime.of(taskRequest.getDateIssue(), taskRequest.getTimeIssue());
        LocalDateTime dateTimeDelivery = LocalDateTime.of(taskRequest.getDateDelivery(), taskRequest.getTimeDelivery());

        Discipline discipline = disciplineService.findDisciplineById(Long.parseLong(disciplineId));

        Task task = new Task(taskRequest.getName(), taskRequest.getMaxScore(), dateTimeIssue, dateTimeDelivery,
                taskRequest.getInstructionTask(), discipline, "Не началось");
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