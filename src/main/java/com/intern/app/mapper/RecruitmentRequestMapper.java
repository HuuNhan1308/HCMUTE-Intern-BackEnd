package com.intern.app.mapper;

import com.intern.app.models.dto.request.RecruitmentCreationRequest;
import com.intern.app.models.dto.request.RecruitmentRequestCreationRequest;
import com.intern.app.models.entity.Recruitment;
import com.intern.app.models.entity.RecruitmentRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecruitmentRequestMapper {
    RecruitmentRequest toRecruitmentRequest(RecruitmentRequestCreationRequest recruitmentRequestCreationRequest);
}
