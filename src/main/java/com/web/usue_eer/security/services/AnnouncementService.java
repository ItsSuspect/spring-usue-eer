package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.Announcement;
import com.web.usue_eer.repository.AnnouncementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public void saveAdvertisement(Announcement announcement) {
        announcementRepository.save(announcement);
    }

    public List<Announcement> findAnnouncementsByDisciplineId (Long disciplineId) {
        return announcementRepository.findAnnouncementsByDisciplineId(disciplineId);
    }

    public Announcement findAnnouncementById (Long announcementId) {
        return announcementRepository.findAnnouncementById(announcementId).orElseThrow(() -> new EntityNotFoundException("Ошибка: Объявление с id " + announcementId + " не найдено"));
    }
}
