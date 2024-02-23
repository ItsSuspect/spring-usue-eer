package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.Group;
import com.web.usue_eer.repository.GroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    public Group findGroupByName (String name) {
        return groupRepository.findByName(name).orElseThrow(() -> new RuntimeException("Ошибка: Группа не найдена"));
    }

    public boolean existsByName (String name) {
        return groupRepository.existsByName(name);
    }

    public void saveGroup(Group group) {
        groupRepository.save(group);
    }
}
