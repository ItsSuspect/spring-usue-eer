package com.web.finance.repository;

import java.util.Optional;

import com.web.finance.entities.ERole;
import com.web.finance.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
