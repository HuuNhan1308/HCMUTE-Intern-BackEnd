package com.intern.app.mapper;

import com.intern.app.models.dto.response.MajorResponse;
import com.intern.app.models.entity.Major;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MajorMapper {
    MajorResponse toMajorResponse(Major major);
}
