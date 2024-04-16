package com.web.usue_eer.repository;

import com.web.usue_eer.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksByDisciplineId(Long id);
    Optional<Task> findTaskById(Long id);
    List<Task> findByStatus(String status);
}
