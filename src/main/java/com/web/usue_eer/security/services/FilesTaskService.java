package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.FilesTask;
import com.web.usue_eer.repository.FilesTaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FilesTaskService {
    @Autowired
    private FilesTaskRepository filesTaskRepository;

    public void saveFileTask(FilesTask filesTask) {
        filesTaskRepository.save(filesTask);
    }

    public List<FilesTask> findFilesTasksByTaskId (Long id) {
        return filesTaskRepository.findFilesTasksByTaskId(id);
    }
    public Optional<FilesTask> findFilesTaskById (Long id) {
        return filesTaskRepository.findFilesTaskById(id);
    }
}
