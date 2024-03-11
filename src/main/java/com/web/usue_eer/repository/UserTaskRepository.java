package com.web.usue_eer.repository;

import com.web.usue_eer.entities.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
    List<UserTask> findUserTasksByUserId(Long id);
    List<UserTask> findUserTasksByTaskId(Long id);
    Optional<UserTask> findUserTaskByUserIdAndTaskId(Long userId, Long taskId);
    @Query("SELECT COUNT(ut) FROM UserTask ut JOIN ut.task t WHERE t.discipline.id = :disciplineId AND t.id = :taskId")
    Long countUserTasksByDisciplineIdAndTaskId(@Param("disciplineId") Long disciplineId, @Param("taskId") Long taskId);

    @Query("SELECT COUNT(ut) FROM UserTask ut WHERE ut.status = 'Проверено' AND ut.task.id = :taskId")
    int countByStatusAndTaskId(@Param("taskId") Long taskId);
}
