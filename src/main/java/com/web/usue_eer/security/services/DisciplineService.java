package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.Discipline;
import com.web.usue_eer.repository.DisciplineRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DisciplineService {
    @Autowired
    private DisciplineRepository disciplineRepository;

    public Discipline saveDiscipline(Discipline discipline) {
        return disciplineRepository.save(discipline);
    }
    public Discipline findDisciplineById(Long id) {
        return disciplineRepository.findById(id).get();
    }
}
