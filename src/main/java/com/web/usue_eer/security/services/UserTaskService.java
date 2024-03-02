package com.web.usue_eer.security.services;

import com.web.usue_eer.repository.UserTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTaskService {
    @Autowired
    private UserTaskRepository userTaskRepository;
}
