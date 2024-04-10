package com.web.usue_eer.repository;

import com.web.usue_eer.entities.FilesTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilesTaskRepository extends JpaRepository<FilesTask, Long> {
    List<FilesTask> findFilesTasksByTaskId(Long id);
    Optional<FilesTask> findFilesTaskById(Long id);
}
