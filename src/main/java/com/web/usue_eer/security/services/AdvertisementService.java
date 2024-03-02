package com.web.usue_eer.security.services;

import com.web.usue_eer.repository.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvertisementService {
    @Autowired
    private AdvertisementRepository advertisementRepository;
}
