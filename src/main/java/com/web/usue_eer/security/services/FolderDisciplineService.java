package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.FolderDiscipline;
import com.web.usue_eer.entities.FolderUser;
import com.web.usue_eer.repository.FolderDisciplineRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Service
public class FolderDisciplineService {
    @Autowired
    private FolderDisciplineRepository folderDisciplineRepository;

    public void saveFolder(FolderDiscipline folder) {
        folderDisciplineRepository.save(folder);
    }

    public List<FolderDiscipline> findAllSubFolders (Long parentFolderId) {
        List<FolderDiscipline> allSubFolders = new ArrayList<>();
        Deque<Long> stack = new ArrayDeque<>();
        stack.push(parentFolderId);

        while (!stack.isEmpty()) {
            Long currentFolderId = stack.pop();
            List<FolderDiscipline> subFolders = folderDisciplineRepository.findAllByParentFolderId(currentFolderId);
            allSubFolders.addAll(subFolders);

            for (FolderDiscipline folder : subFolders) {
                stack.push(folder.getId());
            }
        }

        return allSubFolders;
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
