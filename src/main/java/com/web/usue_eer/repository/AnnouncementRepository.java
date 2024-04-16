package com.web.usue_eer.repository;

import com.web.usue_eer.entities.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findAnnouncementsByDisciplineId(Long id);
    Optional<Announcement> findAnnouncementById(Long id);
}
