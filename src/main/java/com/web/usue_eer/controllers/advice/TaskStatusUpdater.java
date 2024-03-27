package com.web.usue_eer.controllers.advice;

import com.web.usue_eer.entities.Task;
import com.web.usue_eer.security.services.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TaskStatusUpdater {
    private final TaskService taskService;
    @Autowired
    public TaskStatusUpdater(TaskService taskService) {
        this.taskService = taskService;
    }

    @Transactional
    @Scheduled(fixedRate = 900000)
    public void updateTaskStatus() {
        LocalDateTime currentTime = LocalDateTime.now();

        updateTaskNotStarted(currentTime);
        updateTaskInProcess(currentTime);
    }

    public void updateTaskNotStarted(LocalDateTime currentTime) {
        List<Task> tasksNotStarted = taskService.findTasksByStatus("Не началось");

        for (Task task : tasksNotStarted) {
            LocalDateTime issueTime = task.getDateTimeIssue();
            if (issueTime != null && issueTime.isBefore(currentTime)) {
                task.setStatus("В процессе");
                taskService.saveTask(task);
            }
        }
    }

    public void updateTaskInProcess(LocalDateTime currentTime) {
        List<Task> tasksInProcess = taskService.findTasksByStatus("В процессе");

        for (Task task : tasksInProcess) {
            LocalDateTime deliveryTime = task.getDateTimeDelivery();
            if (deliveryTime != null && deliveryTime.isBefore(currentTime)) {
                task.setStatus("Завершено");
                taskService.saveTask(task);
            }
        }
    }
}
