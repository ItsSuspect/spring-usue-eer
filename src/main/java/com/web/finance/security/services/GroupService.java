package com.web.finance.security.services;

import com.web.finance.repository.GroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    public List<String> findGroupNamesByUsername(String username) {
        return groupRepository.findGroupNamesByUsername(username);
    }
}
