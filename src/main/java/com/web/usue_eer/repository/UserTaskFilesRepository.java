package com.web.usue_eer.repository;

import com.web.usue_eer.entities.UserTaskFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTaskFilesRepository extends JpaRepository<UserTaskFiles, Long> {
    List<UserTaskFiles> findUserTaskFilesByUserTaskId(Long id);
    Optional<UserTaskFiles> findUserTaskFilesById(Long id);
}
