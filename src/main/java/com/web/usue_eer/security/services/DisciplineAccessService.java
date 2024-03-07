package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.DisciplineAccess;
import com.web.usue_eer.entities.enums.AccessType;
import com.web.usue_eer.repository.DisciplineAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisciplineAccessService {
    @Autowired
    private DisciplineAccessRepository disciplineAccessRepository;

    public void saveAccess(DisciplineAccess disciplineAccess) {
        disciplineAccessRepository.save(disciplineAccess);
    }

    public DisciplineAccess findByDisciplineIdAndUserId(Long disciplineId, Long userId) {
        return disciplineAccessRepository.findByDisciplineIdAndUserId(disciplineId, userId);
    }
}
