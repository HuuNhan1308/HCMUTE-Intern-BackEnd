package com.intern.app.mapper;

import com.intern.app.models.dto.request.RecruitmentCreationRequest;
import com.intern.app.models.dto.response.RecruitmentResponse;
import com.intern.app.models.entity.Recruitment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RecruitmentMapper {
    Recruitment toRecruitment(RecruitmentCreationRequest recruitmentCreationRequest);

    RecruitmentResponse toRecruitmentResponse(Recruitment recruitment);
}
