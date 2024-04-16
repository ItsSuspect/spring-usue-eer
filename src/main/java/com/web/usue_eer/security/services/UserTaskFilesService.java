package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.UserTaskFiles;
import com.web.usue_eer.repository.UserTaskFilesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserTaskFilesService {
    private final UserTaskFilesRepository userTaskFilesRepository;

    @Autowired
    public UserTaskFilesService(UserTaskFilesRepository userTaskFilesRepository) {
        this.userTaskFilesRepository = userTaskFilesRepository;
    }

    public void saveUserTaskFile(UserTaskFiles userTaskFiles) {
        userTaskFilesRepository.save(userTaskFiles);
    }

    public List<UserTaskFiles> findUserTaskFilesByUserTaskId (Long id) {
        return userTaskFilesRepository.findUserTaskFilesByUserTaskId(id);
    }

    public Optional<UserTaskFiles> findUserTaskFilesById (Long id) {
        return userTaskFilesRepository.findUserTaskFilesById(id);
    }
}
