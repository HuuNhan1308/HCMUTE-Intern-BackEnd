package com.intern.app.mapper;

import com.intern.app.models.dto.request.FacultyRequest;
import com.intern.app.models.dto.response.FacultyResponse;
import com.intern.app.models.entity.Faculty;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FacultyMapper {
    FacultyResponse toFacultyResponse(Faculty faculty);
    Faculty toFaculty(FacultyRequest facultyRequest);
    void updateFaculty(@MappingTarget Faculty faculty, FacultyRequest facultyRequest);
}
