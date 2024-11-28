package com.intern.app.mapper;

import com.intern.app.models.dto.response.RolePermissionResponse;
import com.intern.app.models.entity.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {
    @Mappings({
            @Mapping(target = "dateCreated", source = "dateCreated")
    })
    RolePermissionResponse toRolePermissionResponse(RolePermission rolePermission);
}
