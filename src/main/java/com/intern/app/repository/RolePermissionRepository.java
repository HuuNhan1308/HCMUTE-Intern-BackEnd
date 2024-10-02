package com.intern.app.repository;

import com.intern.app.models.entity.Permission;
import com.intern.app.models.entity.Role;
import com.intern.app.models.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, String> {
    Optional<RolePermission> findByRoleAndPermissionAndDeletedFalse(Role role, Permission permission);
}