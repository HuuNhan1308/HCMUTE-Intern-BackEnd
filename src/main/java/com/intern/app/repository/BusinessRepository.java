package com.intern.app.repository;

import com.intern.app.models.entity.Business;
import com.intern.app.repository.CustomRepository.AppRepository;

import java.util.Optional;

public interface BusinessRepository extends AppRepository<Business, String> {
    Optional<Business> findByName(String name);
}
