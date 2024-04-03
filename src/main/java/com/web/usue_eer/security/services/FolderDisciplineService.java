package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.FolderDiscipline;
import com.web.usue_eer.repository.FolderDisciplineRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderDisciplineService {
    @Autowired
    private FolderDisciplineRepository folderDisciplineRepository;

    public void saveFolder(FolderDiscipline folder) {
        folderDisciplineRepository.save(folder);
    }

    public List<FolderDiscipline> findFolderDisciplinesByDisciplineId (Long id) {
        return folderDisciplineRepository.findFolderDisciplinesByDisciplineId(id);
    }

    public FolderDiscipline findRootFolderByDisciplineId (Long id) {
        return folderDisciplineRepository.findFolderDisciplineByDisciplineIdAndParentFolderIsNull(id);
    }

    public FolderDiscipline findFolderDisciplineById (Long id) {
        return folderDisciplineRepository.findFolderDisciplineById(id);
    }

    @Transactional
    public void deleteFolderDisciplineById (Long id) {
        folderDisciplineRepository.deleteFolderDisciplineById(id);
    }
}
