package com.web.usue_eer.repository;

import com.web.usue_eer.entities.FolderDiscipline;
import com.web.usue_eer.entities.FolderUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public interface FolderUserRepository extends JpaRepository<FolderUser, Long> {
    List<FolderUser> findAllByParentFolderId(Long parentFolderId);
    List<FolderUser> findFolderUsersByUserId(Long id);
    FolderUser findFolderUserByUserIdAndParentFolderIsNull(Long id);
    FolderUser findFolderUserById(Long id);
    void deleteFolderUserById(Long id);
}
