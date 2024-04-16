package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.Information;
import com.web.usue_eer.repository.InformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InformationService {
    private final InformationRepository informationRepository;

    @Autowired
    public InformationService(InformationRepository informationRepository) {
        this.informationRepository = informationRepository;
    }

    public void saveInformation (Information information) {
        informationRepository.save(information);
    }

    public Optional<Information> findInformationByDisciplineId (Long id) {
        return informationRepository.findInformationByDisciplineId(id);
    }
}
