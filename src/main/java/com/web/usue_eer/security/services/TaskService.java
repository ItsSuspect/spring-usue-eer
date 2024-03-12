package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.Task;
import com.web.usue_eer.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    public List<Task> findTasksByDisciplineId (Long id) {
        return taskRepository.findTasksByDisciplineId(id);
    }

    public Task findTaskById (Long id) {
        return taskRepository.findTaskById(id);
    }

    public List<Task> findTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }
}
