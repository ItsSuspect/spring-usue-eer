package com.web.usue_eer.security.services;

import com.web.usue_eer.entities.Discipline;
import com.web.usue_eer.repository.DisciplineRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DisciplineService {
    private final DisciplineRepository disciplineRepository;

    @Autowired
    public DisciplineService(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }

    public void saveDiscipline(Discipline discipline) {
        disciplineRepository.save(discipline);
    }
    public Discipline findDisciplineById(Long disciplineId) {
        return disciplineRepository.findDisciplineById(disciplineId).orElseThrow(() -> new EntityNotFoundException("Ошибка: Дисциплина с id " + disciplineId + " не найдена"));
    }
    public List<Discipline> findDisciplinesByAccess(boolean access) {
        return disciplineRepository.findDisciplinesByAccess(access);
    }
}
