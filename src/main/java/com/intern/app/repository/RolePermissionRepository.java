package com.intern.app.repository;

import com.intern.app.models.dto.response.RolePermissionResponse;
import com.intern.app.models.entity.Permission;
import com.intern.app.models.entity.Role;
import com.intern.app.models.entity.RolePermission;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolePermissionRepository extends AppRepository<RolePermission, String>, JpaSpecificationExecutor<RolePermission> {
    Optional<RolePermission> findByRoleAndPermissionAndDeletedFalse(Role role, Permission permission);
    List<RolePermissionResponse> findAllByRoleRoleId(String roleId);
}