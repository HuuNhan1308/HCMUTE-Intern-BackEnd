package com.intern.app.repository;

import com.intern.app.models.entity.Business;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BusinessRepository extends AppRepository<Business, String>, JpaSpecificationExecutor<Business> {
    Optional<Business> findByName(String name);
}
