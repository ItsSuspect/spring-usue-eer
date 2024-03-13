package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.UserTask;
import com.web.usue_eer.repository.UserTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserTaskService {
    @Autowired
    private UserTaskRepository userTaskRepository;

    public List<UserTask> findUserTasksByUserId (Long id) {
        return userTaskRepository.findUserTasksByUserId(id);
    }

    public List<UserTask> findUserTasksByTaskId (Long id) {
        return userTaskRepository.findUserTasksByTaskId(id);
    }
    public Optional<UserTask> findUserTaskByUserIdAndTaskId (Long userId, Long taskId) {
        return userTaskRepository.findUserTaskByUserIdAndTaskId(userId, taskId);
    }

    public int countUserTasksByDisciplineIdAndTaskId (Long disciplineId, Long taskId) {
        return userTaskRepository.countUserTasksByDisciplineIdAndTaskId(disciplineId, taskId);
    }

    public int countByStatusAndTaskId (Long taskId) {
        return userTaskRepository.countByStatusAndTaskId(taskId);
    }

    public void saveUserTask(UserTask userTask) {
        userTaskRepository.save(userTask);
    }
}