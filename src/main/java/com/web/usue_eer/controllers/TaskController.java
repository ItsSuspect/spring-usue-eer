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

    @Autowired
    private UserNotificationService userNotificationService;

    @GetMapping("/{disciplineId}/task-list/create")
    public String getTaskCreate(Model model, @PathVariable String disciplineId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);
        List<UserNotification> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId());

        model.addAttribute("notifications", userNotifications);
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
                        taskListResponse.setCountSend(userTaskService.countUserTasksByDisciplineIdAndTaskId(Long.parseLong(disciplineId), task.getId()));
                        taskListResponse.setCountChecked(userTaskService.countByStatusAndTaskId(task.getId()));
                    }

                    if (taskListResponse.getStatus() == null) {
                        taskListResponse.setStatus(task.getStatus());
                    }

                    return taskListResponse;
                }).sorted(Comparator.comparing(TaskListResponse::getName)).collect(Collectors.toList());

        List<UserNotification> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId());

        model.addAttribute("notifications", userNotifications);
        model.addAttribute("authorities", authorities);
        model.addAttribute("tasks", taskListResponses);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/task-list");
        return "index";
    }

    @GetMapping("/{disciplineId}/task-list/{taskId}/completed-tasks")
    public String getSendingTasks(Model model, @PathVariable String disciplineId, @PathVariable String taskId) {
        Task task = taskService.findTaskById(Long.parseLong(taskId));
        List<UserTask> userTasks = userTaskService.findUserTasksByTaskId(Long.parseLong(taskId));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        List<CompletedTaskResponse> completedTaskResponses = userTasks.stream()
                .map(userTask -> new CompletedTaskResponse(
                        Long.parseLong(taskId),
                        userTask.getUser(),
                        userTask.getStatus(),
                        userTask.getResultScore(),
                        task.getMaxScore(),
                        userTask.getDateDelivery().format(formatter)
                )).sorted(Comparator.comparing(CompletedTaskResponse::getStatus)).collect(Collectors.toList());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);
        List<UserNotification> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId());

        model.addAttribute("notifications", userNotifications);
        model.addAttribute("tasks", completedTaskResponses);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/completed-tasks");
        return "index";
    }

    @GetMapping("/{disciplineId}/task-list/{taskId}/{username}/task-check")
    public String getSendTask(Model model, @PathVariable String disciplineId, @PathVariable String taskId, @PathVariable String username) {
        User student = userDetailsService.findUserByUsername(username);
        Task task = taskService.findTaskById(Long.parseLong(taskId));
        UserTask userTask = userTaskService.findUserTaskByUserIdAndTaskId(student.getId(), Long.parseLong(taskId)).get();

        TaskCheckResponse taskCheckResponse = new TaskCheckResponse(
                Long.parseLong(taskId),
                task.getName(),
                userTask.getResultScore(),
                task.getMaxScore(),
                userTask.getCommentStudent(),
                userTask.getCommentTeacher(),
                task.getInstruction(),
                student
        );

        String usernameNotification = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(usernameNotification);
        List<UserNotification> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId());

        model.addAttribute("notifications", userNotifications);
        model.addAttribute("task", taskCheckResponse);
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/task-check");
        return "index";
    }

    @GetMapping("/{disciplineId}/task-list/{taskId}")
    public String getTask(Model model, @PathVariable String disciplineId, @PathVariable String taskId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);
        long userId = user.getId();

        UserDiscipline userDiscipline = userDisciplineService.findByDisciplineIdAndUserId(Long.parseLong(disciplineId), userId);
        boolean authorities = getAuthorities(userDiscipline.getAccessType());

        long parsedTaskId = Long.parseLong(taskId);

        Optional<UserTask> userTaskOptional = userTaskService.findUserTaskByUserIdAndTaskId(userId, parsedTaskId);
        Task task = taskService.findTaskById(parsedTaskId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        TaskResponse taskResponse = new TaskResponse(
                parsedTaskId,
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

        List<UserNotification> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId());

        model.addAttribute("notifications", userNotifications);
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

        List<UserNotification> userNotifications = userNotificationService.findUserNotificationsByUserId(user.getId());

        model.addAttribute("notifications", userNotifications);
        model.addAttribute("task", task);
        model.addAttribute("authorities", getAuthorities(userDiscipline.getAccessType()));
        model.addAttribute("disciplines", getDisciplines());
        model.addAttribute("content", "fragments/edit-task");
        return "index";
    }

    //Todo: Прописать валидацию, что нельзя делать дату выдачи позже даты сдачи
    @PostMapping("/{disciplineId}/task-list/{taskId}/edit")
    public ResponseEntity<Void> postEditTask(Model model, @PathVariable String disciplineId, @PathVariable String taskId, @RequestBody TaskRequest taskRequest) {
        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime dateTimeIssue = LocalDateTime.of(taskRequest.getDateIssue(), taskRequest.getTimeIssue());
        LocalDateTime dateTimeDelivery = LocalDateTime.of(taskRequest.getDateDelivery(), taskRequest.getTimeDelivery());
        Task task = taskService.findTaskById(Long.parseLong(taskId));

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

            UserNotification userNotification = new UserNotification(userTask.getUser(), "Выставлена оценка", "task");
            userNotification.setUserTask(userTask);
            userNotificationService.saveNotification(userNotification);
        }

        return ResponseEntity.ok().build();
    }

    //Todo: Прописать валидацию, что нельзя делать дату выдачи позже даты сдачи
    @PostMapping("/{disciplineId}/task-list/create")
    public ResponseEntity<Void> createTask(@PathVariable String disciplineId, @RequestBody TaskRequest taskRequest) {
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

        Discipline discipline = disciplineService.findDisciplineById(Long.parseLong(disciplineId));

        Task task = new Task(taskRequest.getName(), taskRequest.getMaxScore(), dateTimeIssue, dateTimeDelivery,
                taskRequest.getInstructionTask(), discipline, status);
        taskService.saveTask(task);
        return ResponseEntity.ok().build();
    }

    public boolean getAuthorities(AccessType accessType) {
        return accessType.name().equals("LEADER");
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