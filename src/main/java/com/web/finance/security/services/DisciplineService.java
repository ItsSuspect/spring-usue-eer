package com.web.finance.security.services;

import com.web.finance.entities.Discipline;
import com.web.finance.repository.DisciplineRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DisciplineService {
    @Autowired
    private DisciplineRepository disciplineRepository;

    public List<Discipline> findDisciplinesNamesByUsername(String username) {
        return disciplineRepository.findDisciplinesByUsername(username);
    }
}
