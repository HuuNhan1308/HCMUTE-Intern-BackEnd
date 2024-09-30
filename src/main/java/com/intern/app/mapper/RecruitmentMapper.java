package com.intern.app.mapper;

import com.intern.app.models.dto.request.RecruitmentCreationRequest;
import com.intern.app.models.entity.Recruitment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecruitmentMapper {
    Recruitment toRecruitment(RecruitmentCreationRequest recruitmentCreationRequest);
}
