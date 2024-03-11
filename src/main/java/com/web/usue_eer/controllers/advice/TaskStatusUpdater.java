package com.web.usue_eer.controllers.advice;

import com.web.usue_eer.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskStatusUpdater {
    @Autowired
    private TaskRepository taskRepository;

    @Transactional
    @Scheduled(fixedRate = 900000)
    public void updateTaskStatus() {
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println(currentTime);
    }
}
