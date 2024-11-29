package com.intern.app.mapper;

import com.intern.app.models.dto.request.FacultyRequest;
import com.intern.app.models.dto.response.FacultyResponse;
import com.intern.app.models.dto.response.PermissionResponse;
import com.intern.app.models.dto.response.RolePermissionResponse;
import com.intern.app.models.dto.response.RoleResponse;
import com.intern.app.models.entity.Faculty;
import com.intern.app.models.entity.Permission;
import com.intern.app.models.entity.Role;
import com.intern.app.models.entity.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FacultyMapper {
    FacultyResponse toFacultyResponse(Faculty faculty);
    Faculty toFaculty(FacultyRequest facultyRequest);
    void updateFaculty(@MappingTarget Faculty faculty, FacultyRequest facultyRequest);
}

//@Mapper(componentModel = "spring")
//public interface RoleMapper {
//    RoleResponse toRoleResponse(Role role);
//}
//
//@Mapper(componentModel = "spring")
//public interface PermissionMapper {
//    PermissionResponse toPermissionResponse(Permission permission);
//}

