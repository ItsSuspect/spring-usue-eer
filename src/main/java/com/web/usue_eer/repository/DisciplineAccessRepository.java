package com.web.usue_eer.repository;

import com.web.usue_eer.entities.DisciplineAccess;
import com.web.usue_eer.entities.enums.AccessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplineAccessRepository extends JpaRepository<DisciplineAccess, Long> {
    DisciplineAccess findByDisciplineIdAndUserId(Long disciplineId, Long userId);
}
