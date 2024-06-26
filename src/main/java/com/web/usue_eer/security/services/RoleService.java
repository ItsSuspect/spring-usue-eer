package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.Role;
import com.web.usue_eer.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findRoleByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Ошибка: Роль " + name + " не найдена"));
    }
}
