package com.web.usue_eer.repository;

import com.web.usue_eer.entities.UserDiscipline;
import com.web.usue_eer.entities.enums.AccessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDisciplineRepository extends JpaRepository<UserDiscipline, Long> {
    UserDiscipline findByDisciplineIdAndUserId(Long disciplineId, Long userId);
    List<UserDiscipline> findUserDisciplinesByAccessTypeAndDisciplineId(AccessType accessType, Long id);
    @Query("SELECT COUNT(ud) FROM UserDiscipline ud WHERE ud.discipline.id = :disciplineId")
    Long countStudentsByDisciplineId(@Param("disciplineId") Long disciplineId);
}
