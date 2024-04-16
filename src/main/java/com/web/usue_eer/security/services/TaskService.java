package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.Task;
import com.web.usue_eer.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    public List<Task> findTasksByDisciplineId (Long id) {
        return taskRepository.findTasksByDisciplineId(id);
    }

    public Task findTaskById (Long taskId) {
        return taskRepository.findTaskById(taskId).orElseThrow(() -> new EntityNotFoundException("Ошибка: Задание с id " + taskId + " не найдено"));
    }

    public List<Task> findTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }
}
