package com.intern.app.mapper;

import com.intern.app.models.dto.request.ProfileCreationRequest;
import com.intern.app.models.dto.request.StudentCreationRequest;
import com.intern.app.models.dto.request.StudentUpdateRequest;
import com.intern.app.models.dto.response.StudentResponse;
import com.intern.app.models.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentResponse toStudentResponse(Student student);

    Student toStudent(StudentCreationRequest studentCreationRequest);
    Student toStudent(StudentUpdateRequest studentUpdateRequest);

    @Mapping(target = "profile", ignore = true)
    void updateStudent (@MappingTarget Student student, StudentUpdateRequest studentUpdateRequest);
}
