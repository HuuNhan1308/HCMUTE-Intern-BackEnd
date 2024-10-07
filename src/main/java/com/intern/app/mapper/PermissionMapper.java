package com.intern.app.mapper;

import com.intern.app.models.dto.request.PermissionCreationRequest;
import com.intern.app.models.dto.response.MajorResponse;
import com.intern.app.models.entity.Major;
import com.intern.app.models.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionCreationRequest permissionCreationRequest);
}
