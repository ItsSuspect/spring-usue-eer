package com.web.usue_eer.controllers;

import com.web.usue_eer.entities.*;
import com.web.usue_eer.entities.enums.AccessType;
import com.web.usue_eer.payload.request.SendTaskRequest;
import com.web.usue_eer.payload.request.SendTaskTeacherRequest;
import com.web.usue_eer.payload.request.TaskRequest;
import com.web.usue_eer.payload.response.CompletedTaskResponse;
import com.web.usue_eer.payload.response.TaskCheckResponse;
import com.web.usue_eer.payload.response.TaskListResponse;
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
    private final UserDetailsServiceImpl userDetailsService;
    private final DisciplineService disciplineService;
    private final TaskService taskService;
    private final UserTaskService userTaskService;
    private final UserDisciplineService userDisciplineService;
    private final UserNotificationService userNotificationService;
    @Autowired
    public TaskController(UserDetailsServiceImpl userDetailsService, DisciplineService disciplineService, TaskService taskService, UserTaskService userTaskService, UserDisciplineService userDisciplineService, UserNotificationService userNotificationService) {
        this.userDetailsService = userDetailsService;
        this.disciplineService = disciplineService;
        this.taskService = taskService;
        this.userTaskService = userTaskService;
        this.userDisciplineService = userDisciplineService;
        this.userNotificationService = userNotificationService;
    }

    @GetMapping("/{disciplineId}/task-list") //Список всех заданий
    public String getTaskList(Model model, @PathVariable Long disciplineId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        List<Task> tasks = taskService.findTasksByDisciplineId(disciplineId);
        List<UserTask> userTasks = userTaskService.findUserTasksByUserId(user.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(disciplineId, user.getId());
        boolean authorities = getAuthorities(userDiscipline.getAccessType());

        List<TaskListResponse> taskListResponses = tasks.stream()
                .map(task -> {
                    TaskListResponse taskListResponse = new TaskListResponse(
                            task.getId(),
                            task.getName(),
                            task.getMaxScore(),
                            task.getDateTimeDelivery().format(formatter),
                            task.getDateTimeIssue().format(formatter)
                    );

                    if (!authorities) {
                        for (UserTask userTask : userTasks) {
                            if (userTask.getTask().getId().equals(task.getId())) {
                                taskListResponse.setStatus(userTask.getStatus());
                                taskListResponse.setResultScore(userTask.getResultScore());
                                break;
                            }
                        }
                    } else {
                        taskListResponse.setCountSend(userTaskService.countUserTasksByDisciplineIdAndTaskId(disciplineId, task.getId()));
                        taskListResponse.setCountChecked(userTaskService.countByStatusAndTaskId(task.getId()));
                    }

                    if (taskListResponse.getStatus() == null) taskListResponse.setStatus(task.getStatus());

                    return taskListResponse;
                }).sorted(Comparator.comparing(TaskListResponse::getName)).collect(Collectors.toList());

        model.addAttribute("authorities", authorities);
        model.addAttribute("tasks", taskListResponses);
        model.addAttribute("content", "fragments/task-list");
        return "index";
    }

    @GetMapping("/{disciplineId}/task-list/create") //Вывод блока создания задания
    public String getTaskCreate(Model model, @PathVariable Long disciplineId) {
        model.addAttribute("content", "fragments/create-task");
        return "index";
    }

    //Todo: Прописать валидацию, что нельзя делать дату выдачи позже даты сдачи
    @PostMapping("/{disciplineId}/task-list/create")
    public ResponseEntity<Void> createTask(@PathVariable Long disciplineId, @RequestBody TaskRequest taskRequest) {
        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime dateTimeIssue = LocalDateTime.of(taskRequest.getDateIssue(), taskRequest.getTimeIssue());
        LocalDateTime dateTimeDelivery = LocalDateTime.of(taskRequest.getDateDelivery(), taskRequest.getTimeDelivery());
        String status = "Не началось";
        if (dateTimeIssue.isBefore(currentTime)) {
            status = "В процессе";
        }
        if (dateTimeDelivery.isBefore(currentTime)) {
            status = "Завершено";
        }

        Discipline discipline = disciplineService.findDisciplineById(disciplineId);

        Task task = new Task(taskRequest.getName(), taskRequest.getMaxScore(), dateTimeIssue, dateTimeDelivery,
                taskRequest.getInstructionTask(), discipline, status);
        taskService.saveTask(task);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{disciplineId}/task-list/{taskId}/completed-tasks")
    public String getSendingTasks(Model model, @PathVariable Long disciplineId, @PathVariable Long taskId) {
        Task task = taskService.findTaskById(taskId);
        List<UserTask> userTasks = userTaskService.findUserTasksByTaskId(taskId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        List<CompletedTaskResponse> completedTaskResponses = userTasks.stream()
                .map(userTask -> new CompletedTaskResponse(
                        taskId,
                        userTask.getUser(),
                        userTask.getStatus(),
                        userTask.getResultScore(),
                        task.getMaxScore(),
                        userTask.getDateDelivery().format(formatter)
                )).sorted(Comparator.comparing(CompletedTaskResponse::getStatus)).collect(Collectors.toList());

        model.addAttribute("tasks", completedTaskResponses);
        model.addAttribute("content", "fragments/completed-tasks");
        return "index";
    }

    @GetMapping("/{disciplineId}/task-list/{taskId}/{username}/task-check")
    public String getSendTask(Model model, @PathVariable Long disciplineId, @PathVariable Long taskId, @PathVariable String username) {
        User student = userDetailsService.findUserByUsername(username);
        Task task = taskService.findTaskById(taskId);
        UserTask userTask = userTaskService.findUserTaskByUserIdAndTaskId(student.getId(), taskId).get();

        TaskCheckResponse taskCheckResponse = new TaskCheckResponse(
                taskId,
                task.getName(),
                userTask.getResultScore(),
                task.getMaxScore(),
                userTask.getCommentStudent(),
                userTask.getCommentTeacher(),
                task.getInstruction(),
                student
        );

        model.addAttribute("task", taskCheckResponse);
        model.addAttribute("content", "fragments/task-check");
        return "index";
    }

    @GetMapping("/{disciplineId}/task-list/{taskId}")
    public String getTask(Model model, @PathVariable Long disciplineId, @PathVariable Long taskId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);
        long userId = user.getId();

        UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(disciplineId, userId);
        boolean authorities = getAuthorities(userDiscipline.getAccessType());

        Optional<UserTask> userTaskOptional = userTaskService.findUserTaskByUserIdAndTaskId(userId, taskId);
        Task task = taskService.findTaskById(taskId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        TaskResponse taskResponse = new TaskResponse(
                taskId,
                task.getInstruction(),
                task.getName(),
                task.getStatus(),
                task.getMaxScore(),
                task.getDateTimeDelivery().format(formatter),
                task.getDateTimeIssue().format(formatter),
                user,
                false
        );

        userTaskOptional.ifPresent(userTask -> {
            taskResponse.setDateDelivery(userTask.getDateDelivery().format(formatter));
            taskResponse.setStatus(userTask.getStatus());
            taskResponse.setCommentStudent(userTask.getCommentStudent());
            taskResponse.setCommentTeacher(userTask.getCommentTeacher());
            taskResponse.setResultScore(userTask.getResultScore());
            taskResponse.setSending(true);
        });

        model.addAttribute("task", taskResponse);
        model.addAttribute("authorities", authorities);
        model.addAttribute("content", "fragments/task");
        return "index";
    }

    @PostMapping("/{disciplineId}/task-list/{taskId}/send")
    public ResponseEntity<Void> sendingTask(@PathVariable Long disciplineId, @PathVariable Long taskId, @RequestBody SendTaskRequest sendTaskRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        Task task = taskService.findTaskById(taskId);
        LocalDateTime localDateTime = LocalDateTime.now().withSecond(0).withNano(0);

        UserTask userTask = new UserTask(user, task, "Сдано", localDateTime, sendTaskRequest.getComment());
        userTaskService.saveUserTask(userTask);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{disciplineId}/task-list/{taskId}/edit")
    public String getEditTask(Model model, @PathVariable Long disciplineId, @PathVariable Long taskId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(disciplineId, user.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        Task task = taskService.findTaskById(taskId);
        task.setFormattedDateTimeDelivery(task.getDateTimeDelivery().format(formatter));
        task.setFormattedDateTimeIssue(task.getDateTimeIssue().format(formatter));

        LocalDateTime localDateTimeIssue = task.getDateTimeIssue();
        Date dateIssue = Date.from(localDateTimeIssue.atZone(ZoneId.systemDefault()).toInstant());
        model.addAttribute("formattedDateIssue", dateIssue);

        LocalDateTime localDateTimeDelivery = task.getDateTimeIssue();
        Date dateDelivery = Date.from(localDateTimeDelivery.atZone(ZoneId.systemDefault()).toInstant());
        model.addAttribute("formattedDateIssue", dateDelivery);

        model.addAttribute("task", task);
        model.addAttribute("authorities", getAuthorities(userDiscipline.getAccessType()));
        model.addAttribute("content", "fragments/edit-task");
        return "index";
    }

    //Todo: Прописать валидацию, что нельзя делать дату выдачи позже даты сдачи
    @PostMapping("/{disciplineId}/task-list/{taskId}/edit")
    public ResponseEntity<Void> postEditTask(@PathVariable Long disciplineId, @PathVariable Long taskId, @RequestBody TaskRequest taskRequest) {
        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime dateTimeIssue = LocalDateTime.of(taskRequest.getDateIssue(), taskRequest.getTimeIssue());
        LocalDateTime dateTimeDelivery = LocalDateTime.of(taskRequest.getDateDelivery(), taskRequest.getTimeDelivery());
        Task task = taskService.findTaskById(taskId);

        if (dateTimeIssue.isBefore(currentTime)) {
            task.setStatus("В процессе");
        } else task.setStatus("Не началось");
        if (dateTimeDelivery.isBefore(currentTime)) {
            task.setStatus("Завершено");
        }

        task.setName(taskRequest.getName());
        task.setMaxScore(taskRequest.getMaxScore());
        task.setInstruction(taskRequest.getInstructionTask());
        task.setDateTimeDelivery(dateTimeDelivery);
        task.setDateTimeIssue(dateTimeIssue);

        taskService.saveTask(task);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{disciplineId}/task-list/{taskId}/answer-teacher")
    public ResponseEntity<Void> answerTask(@PathVariable Long disciplineId, @PathVariable Long taskId, @RequestBody SendTaskTeacherRequest sendTaskTeacherRequest) {
        User user = userDetailsService.findUserByUsername(sendTaskTeacherRequest.getUsername());

        Optional<UserTask> optionalUserTask = userTaskService.findUserTaskByUserIdAndTaskId(user.getId(), taskId);
        System.out.println(optionalUserTask.isPresent());
        if (optionalUserTask.isPresent()) {
            UserTask userTask = optionalUserTask.get();
            userTask.setCommentTeacher(sendTaskTeacherRequest.getCommentTeacher());
            userTask.setResultScore(sendTaskTeacherRequest.getResultScore());
            userTask.setStatus("Проверено");
            userTaskService.saveUserTask(userTask);

            UserNotification userNotification = new UserNotification(userTask.getUser(), "Выставлена оценка", "task");
            userNotification.setUserTask(userTask);
            userNotificationService.saveNotification(userNotification);
        }

        return ResponseEntity.ok().build();
    }

    public boolean getAuthorities(AccessType accessType) {
        return accessType.name().equals("LEADER");
    }
}