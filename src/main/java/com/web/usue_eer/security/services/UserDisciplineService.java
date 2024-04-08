package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.UserDiscipline;
import com.web.usue_eer.entities.enums.AccessType;
import com.web.usue_eer.repository.UserDisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public void deleteById(Long id) {
        userDisciplineRepository.deleteById(id);
    }

    public List<UserDiscipline> findUserDisciplinesByDisciplineId(Long id) {
        return userDisciplineRepository.findUserDisciplinesByDisciplineId(id);
    }

    public List<UserDiscipline> findUserDisciplinesParticipantByDisciplineId(Long id) {
        return userDisciplineRepository.findUserDisciplinesByAccessTypeAndDisciplineId(AccessType.PARTICIPANT, id);
    }

    public UserDiscipline findByDisciplineIdAndUserId(Long disciplineId, Long userId) {
        return userDisciplineRepository.findByDisciplineIdAndUserId(disciplineId, userId);
    }
}
