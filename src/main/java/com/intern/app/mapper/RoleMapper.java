package com.intern.app.mapper;

import com.intern.app.models.dto.response.RoleResponse;
import com.intern.app.models.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toRoleResponse(Role role);
}


