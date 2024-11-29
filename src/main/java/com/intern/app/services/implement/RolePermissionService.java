package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.PermissionMapper;
import com.intern.app.mapper.RoleMapper;
import com.intern.app.mapper.RolePermissionMapper;
import com.intern.app.models.dto.datamodel.FilterSpecification;
import com.intern.app.models.dto.request.PermissionCreationRequest;
import com.intern.app.models.dto.request.RolePermissionCreationRequest;
import com.intern.app.models.dto.response.PermissionResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.dto.response.RolePermissionResponse;
import com.intern.app.models.dto.response.RoleResponse;
import com.intern.app.models.entity.Permission;
import com.intern.app.models.entity.Role;
import com.intern.app.models.entity.RolePermission;
import com.intern.app.repository.PermissionRepository;
import com.intern.app.repository.RolePermissionRepository;
import com.intern.app.repository.RoleRepository;
import com.intern.app.services.interfaces.IRolePermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@RequiredArgsConstructor
@EnableMethodSecurity
public class RolePermissionService implements IRolePermissionService {
    PermissionMapper permissionMapper;
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    RolePermissionRepository rolePermissionRepository;
    RoleMapper roleMapper;
    RolePermissionMapper rolePermissionMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public ReturnResult<Boolean> CreatePermission(PermissionCreationRequest permissionCreationRequest) {
        var result = new ReturnResult<Boolean>();

        Permission permission = permissionMapper.toPermission(permissionCreationRequest);

        if(permissionRepository.findByName(permission.getName()).isPresent()) {
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

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ReturnResult<List<RoleResponse>> GetAllRole() {
        var result = new ReturnResult<List<RoleResponse>>();

        Sort sort = Sort.by(Sort.Direction.ASC, "roleName");
        List<RoleResponse> roles = roleRepository.findAll(sort)
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();

        result.setResult(roles);
        result.setCode(200);

        return result;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ReturnResult<List<PermissionResponse>> GetAllPermission() {
        var result = new ReturnResult<List<PermissionResponse>>();

        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        List<PermissionResponse> permissions = permissionRepository.findAll(sort)
                .stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();

        result.setResult(permissions);
        result.setCode(200);

        return result;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ReturnResult<List<RolePermissionResponse>> GetPermissionByRoleId(String roleId) {
        var result = new ReturnResult<List<RolePermissionResponse>>();

        roleRepository.findById(roleId).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST));

        // Inline specification logic
        Specification<RolePermission> spec = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role").get("roleId"), roleId);
        // Sort by permission.name
        Sort sort = Sort.by(Sort.Direction.ASC, "permission.name");

        List<RolePermissionResponse> permissions = rolePermissionRepository.findAll(spec, sort)
                .stream()
                .map(rolePermission -> {
                    RolePermissionResponse rolePermissionResponse = rolePermissionMapper.toRolePermissionResponse(rolePermission);
                    rolePermissionResponse.setRole(null);
                    return rolePermissionResponse;
                })
                .toList();

        result.setResult(permissions);
        result.setCode(200);

        return result;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ReturnResult<Boolean> DeleteRolePermission(List<String> rolePermissionId) {
        var result = new ReturnResult<Boolean>();

        List<RolePermission> rolePermissions = rolePermissionRepository.findAllById(rolePermissionId);

        rolePermissionRepository.softDeleteRange(rolePermissions);

        result.setResult(Boolean.TRUE);
        result.setCode(200);
        return result;
    }


}


