package com.intern.app.mapper;

import com.intern.app.models.dto.request.StudentCreationRequest;
import com.intern.app.models.dto.request.StudentUpdateRequest;
import com.intern.app.models.dto.response.InstructorResponse;
import com.intern.app.models.dto.response.StudentResponse;
import com.intern.app.models.entity.Instructor;
import com.intern.app.models.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InstructorMapper {
    InstructorResponse toInstructorResponse(Instructor instructor);
}
