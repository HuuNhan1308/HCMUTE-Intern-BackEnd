package com.intern.app.mapper;

import com.intern.app.models.dto.request.MajorRequest;
import com.intern.app.models.dto.response.MajorResponse;
import com.intern.app.models.entity.Major;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MajorMapper {
    MajorResponse toMajorResponse(Major major);
    Major toMajor(MajorRequest majorRequest);
    void updateMajor(@MappingTarget Major major, MajorRequest majorRequest);
}
