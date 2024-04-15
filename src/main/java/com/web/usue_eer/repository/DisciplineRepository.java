package com.web.usue_eer.repository;

import com.web.usue_eer.entities.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
    Optional<Discipline> findById(Long id);
    List<Discipline> findDisciplinesByAccess(boolean access);
}
