package com.web.usue_eer.controllers.DisciplineCategory;

import com.web.usue_eer.entities.*;
import com.web.usue_eer.entities.enums.AccessType;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/portal/discipline")
public class TaskController {
    private static final Logger logger = Logger.getLogger(TaskController.class.getName());
    private final UserDetailsServiceImpl userDetailsService;
    private final DisciplineService disciplineService;
    private final TaskService taskService;
    private final UserTaskService userTaskService;
    private final UserDisciplineService userDisciplineService;
    private final UserNotificationService userNotificationService;
    private final FilesTaskService filesTaskService;
    private final UserTaskFilesService userTaskFilesService;

    @Autowired
    public TaskController(UserDetailsServiceImpl userDetailsService, DisciplineService disciplineService, TaskService taskService, UserTaskService userTaskService, UserDisciplineService userDisciplineService, UserNotificationService userNotificationService, FilesTaskService filesTaskService, UserTaskFilesService userTaskFilesService) {
        this.userDetailsService = userDetailsService;
        this.disciplineService = disciplineService;
        this.taskService = taskService;
        this.userTaskService = userTaskService;
        this.userDisciplineService = userDisciplineService;
        this.userNotificationService = userNotificationService;
        this.filesTaskService = filesTaskService;
        this.userTaskFilesService = userTaskFilesService;
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
        model.addAttribute("content", "discipline-tabs/discipline-tasks/task-list");
        return "index";
    }

    @GetMapping("/{disciplineId}/task-list/create") //Вывод блока создания задания
    public String getTaskCreate(Model model, @PathVariable Long disciplineId) {
        model.addAttribute("content", "discipline-tabs/discipline-tasks/create-task");
        return "index";
    }

    //Todo: Прописать валидацию, что нельзя делать дату выдачи позже даты сдачи
    @PostMapping("/{disciplineId}/task-list/create")
    public ResponseEntity<Void> createTask(@PathVariable Long disciplineId,
                                           @RequestParam(name = "files", required = false) List<MultipartFile> files,
                                           @RequestParam("name") String name,
                                           @RequestParam("maxScore") Integer maxScore,
                                           @RequestParam("dateIssue") LocalDate dateIssue,
                                           @RequestParam("timeIssue") LocalTime timeIssue,
                                           @RequestParam("dateDelivery") LocalDate dateDelivery,
                                           @RequestParam("timeDelivery") LocalTime timeDelivery,
                                           @RequestParam("instructionTask") String instructionTask) {
        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime dateTimeIssue = LocalDateTime.of(dateIssue, timeIssue);
        LocalDateTime dateTimeDelivery = LocalDateTime.of(dateDelivery, timeDelivery);
        String status = "Не началось";
        if (dateTimeIssue.isBefore(currentTime)) {
            status = "В процессе";
        }
        if (dateTimeDelivery.isBefore(currentTime)) {
            status = "Завершено";
        }

        Discipline discipline = disciplineService.findDisciplineById(disciplineId);

        Task task = new Task(name, maxScore, dateTimeIssue, dateTimeDelivery, instructionTask, discipline, status);
        taskService.saveTask(task);

        try {
            for (MultipartFile file : files) {
                FilesTask filesTask = new FilesTask();
                filesTask.setTask(task);
                filesTask.setFileName(file.getOriginalFilename());
                filesTask.setFileData(file.getBytes());
                filesTask.setFileType(file.getContentType());
                filesTask.setFileSize(file.getSize());
                filesTaskService.saveFileTask(filesTask);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при сохранение файла", e);
        }

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
        model.addAttribute("content", "discipline-tabs/discipline-tasks/completed-tasks");
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
        model.addAttribute("content", "discipline-tabs/discipline-tasks/task-check");
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

        List<UserTaskFiles> userTaskFiles = null;
        if (userTaskOptional.isPresent()) {
            UserTask userTask = userTaskOptional.get();

            taskResponse.setDateDelivery(userTask.getDateDelivery().format(formatter));
            taskResponse.setStatus(userTask.getStatus());
            taskResponse.setCommentStudent(userTask.getCommentStudent());
            taskResponse.setCommentTeacher(userTask.getCommentTeacher());
            taskResponse.setResultScore(userTask.getResultScore());
            taskResponse.setSending(true);

            userTaskFiles = userTaskFilesService.findUserTaskFilesByUserTaskId(userTask.getId());
        }
        //TODO: Использовать DTO для файлов. Получать файлы через task.getFilesTasks
        List<FilesTask> filesTasks = filesTaskService.findFilesTasksByTaskId(taskId);

        model.addAttribute("filesTasks", filesTasks);
        model.addAttribute("userTaskFiles", userTaskFiles);
        model.addAttribute("task", taskResponse);
        model.addAttribute("authorities", authorities);
        model.addAttribute("content", "discipline-tabs/discipline-tasks/task");
        return "index";
    }

    @PostMapping("/{disciplineId}/task-list/{taskId}/send")
    public ResponseEntity<Void> sendingTask(@PathVariable Long disciplineId,
                                            @PathVariable Long taskId,
                                            @RequestParam(name = "files", required = false) List<MultipartFile> files,
                                            @RequestParam("comment") String comment) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDetailsService.findUserByUsername(username);

        Task task = taskService.findTaskById(taskId);
        LocalDateTime localDateTime = LocalDateTime.now().withSecond(0).withNano(0);

        UserTask userTask = new UserTask(user, task, "Сдано", localDateTime, comment);
        userTaskService.saveUserTask(userTask);

        try {
            for (MultipartFile file : files) {
                UserTaskFiles filesTask = new UserTaskFiles();
                filesTask.setUserTask(userTask);
                filesTask.setFileName(file.getOriginalFilename());
                filesTask.setFileData(file.getBytes());
                filesTask.setFileType(file.getContentType());
                filesTask.setFileSize(file.getSize());
                userTaskFilesService.saveUserTaskFile(filesTask);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при сохранение файла", e);
        }

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
        model.addAttribute("content", "discipline-tabs/discipline-tasks/edit-task");
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