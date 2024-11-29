package com.intern.app.mapper;

import com.intern.app.models.dto.request.PermissionCreationRequest;
import com.intern.app.models.dto.response.MajorResponse;
import com.intern.app.models.dto.response.PermissionResponse;
import com.intern.app.models.dto.response.RoleResponse;
import com.intern.app.models.entity.Major;
import com.intern.app.models.entity.Permission;
import com.intern.app.models.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionCreationRequest permissionCreationRequest);

    PermissionResponse toPermissionResponse(Permission permission);
}
