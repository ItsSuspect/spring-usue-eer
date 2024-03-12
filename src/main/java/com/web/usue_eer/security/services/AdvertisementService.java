package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.Advertisement;
import com.web.usue_eer.repository.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertisementService {
    @Autowired
    private AdvertisementRepository advertisementRepository;

    public void saveAdvertisement(Advertisement advertisement) {
        advertisementRepository.save(advertisement);
    }

    public List<Advertisement> findAdvertisementsByDisciplineId (Long disciplineId) {
        return advertisementRepository.findAdvertisementsByDisciplineId(disciplineId);
    }

    public Advertisement findAdvertisementById (Long id) {
        return advertisementRepository.findAdvertisementById(id);
    }
}
