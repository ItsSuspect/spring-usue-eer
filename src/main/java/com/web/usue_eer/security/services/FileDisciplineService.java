package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.FileDiscipline;
import com.web.usue_eer.repository.FileDisciplineRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FileDisciplineService {
    @Autowired
    private FileDisciplineRepository fileDisciplineRepository;
    public void saveFile(FileDiscipline file) {
        fileDisciplineRepository.save(file);
    }

    public List<FileDiscipline> findFileDisciplinesByDisciplineId(Long id) {
        return fileDisciplineRepository.findFileDisciplinesByDisciplineId(id);
    }

    public List<FileDiscipline> findFileDisciplinesByFolderId(Long id) {
        return fileDisciplineRepository.findFileDisciplinesByFolderId(id);
    }

    public Optional<FileDiscipline> findFileDisciplineById(Long id) {
        return fileDisciplineRepository.findFileDisciplineById(id);
    }

    public void deleteFileDisciplineById(Long id) {
        fileDisciplineRepository.deleteFileDisciplineById(id);
    }
}
