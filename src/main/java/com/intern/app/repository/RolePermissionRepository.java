package com.intern.app.repository;

import com.intern.app.models.entity.Permission;
import com.intern.app.models.entity.Role;
import com.intern.app.models.entity.RolePermission;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolePermissionRepository extends AppRepository<RolePermission, String> {
    Optional<RolePermission> findByRoleAndPermissionAndDeletedFalse(Role role, Permission permission);
}