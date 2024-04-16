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
    private final FilesTaskRepository filesTaskRepository;

    @Autowired
    public FilesTaskService(FilesTaskRepository filesTaskRepository) {
        this.filesTaskRepository = filesTaskRepository;
    }

    public void saveFileTask(FilesTask filesTask) {
        filesTaskRepository.save(filesTask);
    }

    public void deleteFilesTaskById(Long filesTaskId) {
        filesTaskRepository.deleteFilesTaskById(filesTaskId);
    }

    public List<FilesTask> findFilesTasksByTaskId (Long id) {
        return filesTaskRepository.findFilesTasksByTaskId(id);
    }
    public Optional<FilesTask> findFilesTaskById (Long id) {
        return filesTaskRepository.findFilesTaskById(id);
    }
}
