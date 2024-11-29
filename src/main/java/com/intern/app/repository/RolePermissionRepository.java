package com.intern.app.repository;

import com.intern.app.models.dto.response.RolePermissionResponse;
import com.intern.app.models.entity.Permission;
import com.intern.app.models.entity.Role;
import com.intern.app.models.entity.RolePermission;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolePermissionRepository extends AppRepository<RolePermission, String>, JpaSpecificationExecutor<RolePermission> {
    Optional<RolePermission> findByRoleAndPermissionAndDeletedFalse(Role role, Permission permission);

    @Query("SELECT rp.permission FROM RolePermission rp " +
            "WHERE rp.role.roleId = :roleId AND rp.deleted = false " +
            "ORDER BY rp.permission.name ASC")
    List<Permission> findPermissionsByRoleId(@Param("roleId") String roleId);
}