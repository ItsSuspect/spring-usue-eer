package com.web.usue_eer.repository;

import com.web.usue_eer.entities.FileDiscipline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileDisciplineRepository extends JpaRepository<FileDiscipline, Long> {
    List<FileDiscipline> findFileDisciplinesByDisciplineId(Long id);
    FileDiscipline findFileDisciplineById(Long id);
    List<FileDiscipline> findFileDisciplinesByFolderId(Long id);
    void deleteFileDisciplineById(Long id);
}
