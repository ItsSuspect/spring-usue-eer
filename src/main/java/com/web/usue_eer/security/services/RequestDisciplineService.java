package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.RequestDiscipline;
import com.web.usue_eer.repository.RequestDisciplineRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestDisciplineService {
    private final RequestDisciplineRepository requestDisciplineRepository;

    @Autowired
    public RequestDisciplineService(RequestDisciplineRepository requestDisciplineRepository) {
        this.requestDisciplineRepository = requestDisciplineRepository;
    }

    public void saveRequestDiscipline (RequestDiscipline requestDiscipline) {
        requestDisciplineRepository.save(requestDiscipline);
    }

    public boolean existsRequestDisciplineByUserIdAndDisciplineId (Long userId, Long disciplineId) {
        return requestDisciplineRepository.existsRequestDisciplineByUserIdAndDisciplineId(userId, disciplineId);
    }


    public List<RequestDiscipline> findRequestDisciplinesByDisciplineId (Long disciplineId) {
        return requestDisciplineRepository.findRequestDisciplinesByDisciplineId(disciplineId);
    }

    @Transactional
    public void deleteByDisciplineIdAndUserId (Long disciplineId, Long userId) {
        requestDisciplineRepository.deleteByDisciplineIdAndUserId(disciplineId, userId);
    }
}
