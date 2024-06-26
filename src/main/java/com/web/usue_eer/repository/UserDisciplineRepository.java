package com.web.usue_eer.repository;

import com.web.usue_eer.entities.UserDiscipline;
import com.web.usue_eer.entities.enums.AccessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDisciplineRepository extends JpaRepository<UserDiscipline, Long> {
    Optional<UserDiscipline> findByDisciplineIdAndUserId(Long disciplineId, Long userId);
    boolean existsByDisciplineIdAndUserId(Long disciplineId, Long userId);
    List<UserDiscipline> findUserDisciplinesByUserId(Long id);
    List<UserDiscipline> findUserDisciplinesByAccessTypeAndDisciplineId(AccessType accessType, Long id);
    List<UserDiscipline> findUserDisciplinesByDisciplineId(Long id);
    void deleteById(Long id);
    @Query("SELECT COUNT(ud) FROM UserDiscipline ud WHERE ud.discipline.id = :disciplineId")
    Long countStudentsByDisciplineId(@Param("disciplineId") Long disciplineId);
}
