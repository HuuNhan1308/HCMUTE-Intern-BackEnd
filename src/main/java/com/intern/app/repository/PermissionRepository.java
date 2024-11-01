package com.intern.app.repository;

import com.intern.app.models.entity.Permission;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends AppRepository<Permission, Integer> {
    Optional<Permission> findByName(String name);
    Optional<Permission> findByPermissionIdAndDeletedFalse(Integer id);
}