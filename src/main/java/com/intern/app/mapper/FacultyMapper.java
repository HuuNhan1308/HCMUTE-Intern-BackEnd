package com.intern.app.mapper;

import com.intern.app.models.dto.response.FacultyResponse;
import com.intern.app.models.entity.Faculty;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FacultyMapper {
    FacultyResponse toFacultyResponse(Faculty faculty);
}
