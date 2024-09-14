package com.intern.app.mapper;

import com.intern.app.models.dto.response.StudentResponse;
import com.intern.app.models.entity.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentResponse toStudentResponse(Student student);
}
