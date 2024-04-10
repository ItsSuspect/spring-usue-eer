package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.FolderUser;
import com.web.usue_eer.repository.FolderUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Service
public class FolderUserService {
    @Autowired
    private FolderUserRepository folderUserRepository;

    public void saveFolder(FolderUser folder) {
        folderUserRepository.save(folder);
    }

    public List<FolderUser> findFolderUsersByUserId (Long id) {
        return folderUserRepository.findFolderUsersByUserId(id);
    }

    public List<FolderUser> findAllSubFolders (Long parentFolderId) {
        List<FolderUser> allSubFolders = new ArrayList<>();
        Deque<Long> stack = new ArrayDeque<>();
        stack.push(parentFolderId);

        while (!stack.isEmpty()) {
            Long currentFolderId = stack.pop();
            List<FolderUser> subFolders = folderUserRepository.findAllByParentFolderId(currentFolderId);
            allSubFolders.addAll(subFolders);

            for (FolderUser folder : subFolders) {
                stack.push(folder.getId());
            }
        }

        return allSubFolders;
    }

    public FolderUser findRootFolderByUserId (Long id) {
        return folderUserRepository.findFolderUserByUserIdAndParentFolderIsNull(id);
    }

    public FolderUser findFolderUserById (Long id) {
        return folderUserRepository.findFolderUserById(id);
    }

    @Transactional
    public void deleteFolderUserById (Long id) {
        folderUserRepository.deleteFolderUserById(id);
    }
}
