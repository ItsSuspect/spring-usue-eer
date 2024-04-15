package com.web.usue_eer.repository;

import com.web.usue_eer.entities.RequestDiscipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestDisciplineRepository extends JpaRepository<RequestDiscipline, Long> {
    boolean existsRequestDisciplineByUserIdAndDisciplineId(Long userId, Long disciplineId);
    List<RequestDiscipline> findRequestDisciplinesByDisciplineId(Long id);
    void deleteByDisciplineIdAndUserId(Long disciplineId, Long userId);
}
