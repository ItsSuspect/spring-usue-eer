package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.UserTask;
import com.web.usue_eer.repository.UserTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTaskService {
    @Autowired
    private UserTaskRepository userTaskRepository;

    public List<UserTask> findUserTasksByUserId (Long id) {
        return userTaskRepository.findUserTasksByUserId(id);
    }
}