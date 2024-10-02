package com.intern.app.repository;

import com.intern.app.models.entity.Permission;
import com.intern.app.models.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Optional<Permission> findByNameAndDeletedFalse(String name);
    Optional<Permission> findByPermissionIdAndDeletedFalse(Integer id);
}