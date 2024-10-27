package com.intern.app.mapper;

import com.intern.app.models.dto.response.InstructorRequestResponse;
import com.intern.app.models.entity.InstructorRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface InstructorRequestMapper {
    @Mappings({
            @Mapping(target = "student", ignore = true),
            @Mapping(target = "instructor", ignore = true),
    })
    InstructorRequestResponse toInstructorRequestResponse(InstructorRequest instructorRequest);
}
