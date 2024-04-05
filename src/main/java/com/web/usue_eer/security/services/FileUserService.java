package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.FileUser;
import com.web.usue_eer.repository.FileUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FileUserService {
    @Autowired
    private FileUserRepository fileUserRepository;

    public void saveFile(FileUser file) {
        fileUserRepository.save(file);
    }

    public List<FileUser> findFileUsersByUserId(Long id) {
        return fileUserRepository.findFileUsersByUserId(id);
    }

    public List<FileUser> findFileUsersByFolderId(Long id) {
        return fileUserRepository.findFileUsersByFolderId(id);
    }

    public FileUser findFileUserById(Long id) {
        return fileUserRepository.findFileUserById(id);
    }

    public void deleteFileUserById(Long id) {
        fileUserRepository.deleteFileUserById(id);
    }
}
