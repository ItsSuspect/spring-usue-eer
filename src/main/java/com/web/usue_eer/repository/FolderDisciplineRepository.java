package com.web.usue_eer.repository;

import com.web.usue_eer.entities.FolderDiscipline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderDisciplineRepository extends JpaRepository<FolderDiscipline, Long> {
    List<FolderDiscipline> findFolderDisciplinesByDisciplineId(Long id);
    FolderDiscipline findFolderDisciplineByDisciplineIdAndParentFolderIsNull(Long id);
    FolderDiscipline findFolderDisciplineById(Long id);
    void deleteFolderDisciplineById(Long id);
}
