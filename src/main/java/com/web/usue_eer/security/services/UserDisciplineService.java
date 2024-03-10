package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.UserDiscipline;
import com.web.usue_eer.repository.UserDisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDisciplineService {
    @Autowired
    private UserDisciplineRepository userDisciplineRepository;

    public void saveAccess(UserDiscipline disciplineAccess) {
        userDisciplineRepository.save(disciplineAccess);
    }

    public Long countStudentsByDisciplineId (Long disciplineId) {
        return userDisciplineRepository.countStudentsByDisciplineId(disciplineId);
    }

    public UserDiscipline findByDisciplineIdAndUserId(Long disciplineId, Long userId) {
        return userDisciplineRepository.findByDisciplineIdAndUserId(disciplineId, userId);
    }
}
