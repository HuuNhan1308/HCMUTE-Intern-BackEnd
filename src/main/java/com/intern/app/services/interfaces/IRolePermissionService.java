package com.intern.app.services.interfaces;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.models.dto.request.PermissionCreationRequest;
import com.intern.app.models.dto.request.RolePermissionCreationRequest;
import com.intern.app.models.dto.response.PermissionResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.dto.response.RolePermissionResponse;
import com.intern.app.models.dto.response.RoleResponse;
import com.intern.app.models.entity.Permission;
import com.intern.app.models.entity.Role;
import com.intern.app.models.entity.RolePermission;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface IRolePermissionService {
    ReturnResult<Boolean> CreatePermission(PermissionCreationRequest permissionCreationRequest);

    ReturnResult<Boolean> BindRolePermission(RolePermissionCreationRequest rolePermissionCreationRequest);

    ReturnResult<List<RoleResponse>> GetAllRole();
    ReturnResult<List<PermissionResponse>> GetAllPermission();
    ReturnResult<List<PermissionResponse>> GetPermissionByRoleId(String roleId);
}

