package com.web.usue_eer.repository;

import com.web.usue_eer.entities.FileDiscipline;
import com.web.usue_eer.entities.FileUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileUserRepository extends JpaRepository<FileUser, Long> {
    List<FileUser> findFileUsersByUserId(Long id);
    FileUser findFileUserById(Long id);
    List<FileUser> findFileUsersByFolderId(Long id);
    void deleteFileUserById(Long id);
}
