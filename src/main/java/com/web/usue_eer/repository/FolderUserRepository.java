package com.web.usue_eer.repository;

import com.web.usue_eer.entities.FolderDiscipline;
import com.web.usue_eer.entities.FolderUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderUserRepository extends JpaRepository<FolderUser, Long> {
    List<FolderUser> findFolderUsersByUserId(Long id);
    FolderUser findFolderUserByUserIdAndParentFolderIsNull(Long id);
    FolderUser findFolderUserById(Long id);
    void deleteFolderUserById(Long id);
}
