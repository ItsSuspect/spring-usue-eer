package com.web.usue_eer.repository;

import com.web.usue_eer.entities.FileSharing;
import com.web.usue_eer.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileSharingRepository extends JpaRepository<FileSharing, Long> {
    List<FileSharing> findFileSharingsByUserIdAndDisciplineId(Long userId, Long disciplineId);
    List<FileSharing> findFileSharingsByDisciplineId(Long id);
    Optional<FileSharing> findFileSharingById(Long id);
    void deleteFileSharingById(Long id);
}
