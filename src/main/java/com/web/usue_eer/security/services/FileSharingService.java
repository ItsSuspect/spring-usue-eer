package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.FileSharing;
import com.web.usue_eer.entities.User;
import com.web.usue_eer.repository.FileSharingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FileSharingService {
    @Autowired
    private FileSharingRepository fileSharingRepository;

    public void saveFile(FileSharing file) {
        fileSharingRepository.save(file);
    }

    public List<FileSharing> findFileSharingsByUserIdAndDisciplineId(Long userId, Long disciplineId) {
        return fileSharingRepository.findFileSharingsByUserIdAndDisciplineId(userId, disciplineId);
    }

    public List<FileSharing> findFileSharingsByDisciplineId(Long disciplineId) {
        return fileSharingRepository.findFileSharingsByDisciplineId(disciplineId);
    }

    public Optional<FileSharing> findFileSharingById(Long id) {
        return fileSharingRepository.findFileSharingById(id);
    }

    public void deleteFileSharingById(Long id) {
        fileSharingRepository.deleteFileSharingById(id);
    }
}
