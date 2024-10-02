package com.intern.app.services;

import com.github.javafaker.Bool;
import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.FacultyMapper;
import com.intern.app.mapper.PermissionMapper;
import com.intern.app.models.dto.request.PermissionCreationRequest;
import com.intern.app.models.dto.request.RolePermissionCreationRequest;
import com.intern.app.models.dto.response.FacultyResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Faculty;
import com.intern.app.models.entity.Permission;
import com.intern.app.models.entity.Role;
import com.intern.app.models.entity.RolePermission;
import com.intern.app.repository.FacultyRepository;
import com.intern.app.repository.PermissionRepository;
import com.intern.app.repository.RolePermissionRepository;
import com.intern.app.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@RequiredArgsConstructor
@EnableMethodSecurity
public class RolePermissionService {
    PermissionMapper permissionMapper;
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public ReturnResult<Boolean> CreatePermission(PermissionCreationRequest permissionCreationRequest) {
        var result = new ReturnResult<Boolean>();

        Permission permission = permissionMapper.toPermission(permissionCreationRequest);

        if(permissionRepository.findByNameAndDeletedFalse(permission.getName()).isPresent()) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }
        Permission savedPermission = permissionRepository.save(permission);

        result.setResult(savedPermission.getPermissionId() != null);
        result.setCode(200);

        return result;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ReturnResult<Boolean> BindRolePermission(RolePermissionCreationRequest rolePermissionCreationRequest) {
        var result = new ReturnResult<Boolean>();

        rolePermissionCreationRequest.getRoleIds().forEach(roleId -> {
            rolePermissionCreationRequest.getPermissionIds().forEach(permissionId -> {
                Permission permission = permissionRepository.findByPermissionIdAndDeletedFalse(permissionId).orElse(null);
                Role role =  roleRepository.findByRoleIdAndDeletedFalse(roleId).orElse(null);

                if(permission == null || role == null) {
                    throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
                }

                if(rolePermissionRepository.findByRoleAndPermissionAndDeletedFalse(role, permission).isEmpty()) {
                    RolePermission rolePermission = RolePermission.builder()
                            .role(role)
                            .permission(permission)
                            .build();

                    rolePermissionRepository.save(rolePermission);
                }
            });

        });

        result.setResult(true);
        result.setCode(200);

        return result;
    }

}


