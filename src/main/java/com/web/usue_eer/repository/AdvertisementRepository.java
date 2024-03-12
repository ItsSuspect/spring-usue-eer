package com.web.usue_eer.repository;

import com.web.usue_eer.entities.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findAdvertisementsByDisciplineId(Long id);
    Advertisement findAdvertisementById(Long id);
}
