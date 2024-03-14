package com.web.usue_eer.repository;

import com.web.usue_eer.entities.Information;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InformationRepository  extends JpaRepository<Information, Long> {
    Optional<Information> findInformationByDisciplineId(Long id);
}
