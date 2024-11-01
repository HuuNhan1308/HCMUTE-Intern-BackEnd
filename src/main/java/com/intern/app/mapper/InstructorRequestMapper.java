package com.intern.app.mapper;

import com.intern.app.models.dto.request.InstructorRequestCreationRequest;
import com.intern.app.models.dto.response.InstructorRequestResponse;
import com.intern.app.models.entity.InstructorRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface InstructorRequestMapper {
    @Mappings({
            @Mapping(target = "student", ignore = true),
            @Mapping(target = "instructor", ignore = true),
    })
    InstructorRequestResponse toInstructorRequestResponse(InstructorRequest instructorRequest);

    @Mappings({
            @Mapping(target = "instructor", ignore = true),
            @Mapping(target = "student", ignore = true),
    })
    void updateInstructorRequest(@MappingTarget InstructorRequest instructorRequest, InstructorRequestCreationRequest instructorRequestResponse);
}
