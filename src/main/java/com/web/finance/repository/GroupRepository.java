package com.web.finance.repository;

import com.web.finance.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("SELECT d.name FROM User u JOIN u.groups d WHERE u.username = :username")
    List<String> findGroupNamesByUsername(@Param("username") String username);
}