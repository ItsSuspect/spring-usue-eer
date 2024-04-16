package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.Group;
import com.web.usue_eer.repository.GroupRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GroupService {
    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group findGroupByName (String name) {
        return groupRepository.findByName(name).orElseThrow(() -> new RuntimeException("Ошибка: Группа " + name + " не найдена"));
    }

    public List<Group> findAllGroups () {
        return groupRepository.findAll();
    }

    public boolean existsByName (String name) {
        return groupRepository.existsByName(name);
    }

    public void saveGroup(Group group) {
        groupRepository.save(group);
    }
}
