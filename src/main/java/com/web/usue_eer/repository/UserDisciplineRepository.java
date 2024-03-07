package com.web.usue_eer.repository;

import com.web.usue_eer.entities.UserDiscipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDisciplineRepository extends JpaRepository<UserDiscipline, Long> {
    UserDiscipline findByDisciplineIdAndUserId(Long disciplineId, Long userId);
}
