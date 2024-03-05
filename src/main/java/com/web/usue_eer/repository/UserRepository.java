package com.web.usue_eer.repository;

import java.util.List;
import java.util.Optional;

import com.web.usue_eer.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findAll();

    @Query("SELECT DISTINCT u FROM User u JOIN u.groups g WHERE g.name = :groupName")
    List<User> findUsersByGroupName(@Param("groupName") String groupName);

    @Query("SELECT DISTINCT u FROM User u JOIN u.disciplines d WHERE d.id = :disciplineId")
    List<User> findByDisciplineId(Long disciplineId);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
