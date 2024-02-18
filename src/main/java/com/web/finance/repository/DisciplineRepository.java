package com.web.finance.repository;

import com.web.finance.entities.Discipline;
import com.web.finance.entities.ERole;
import com.web.finance.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
//    @Query("SELECT d.name FROM User u JOIN u.disciplines d WHERE u.username = :username")
//    List<String> findDisciplinesNamesByUsername(String username);

    @Query("SELECT d FROM User u JOIN u.disciplines d WHERE u.username = :username")
    List<Discipline> findDisciplinesByUsername(String username);


    Optional<Discipline> findByName(String name);
}
