package com.intern.app.services.interfaces;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.models.dto.request.PermissionCreationRequest;
import com.intern.app.models.dto.request.RolePermissionCreationRequest;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Permission;
import com.intern.app.models.entity.Role;
import com.intern.app.models.entity.RolePermission;
import org.springframework.security.access.prepost.PreAuthorize;

public interface IRolePermissionService {
    @PreAuthorize("hasRole('ADMIN')")
    public ReturnResult<Boolean> CreatePermission(PermissionCreationRequest permissionCreationRequest);

    @PreAuthorize("hasRole('ADMIN')")
    public ReturnResult<Boolean> BindRolePermission(RolePermissionCreationRequest rolePermissionCreationRequest);
}

