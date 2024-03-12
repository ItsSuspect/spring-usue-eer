package com.web.usue_eer.repository;

import com.web.usue_eer.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksByDisciplineId(Long id);
    Task findTaskById(Long id);
    List<Task> findByStatus(String status);
}
