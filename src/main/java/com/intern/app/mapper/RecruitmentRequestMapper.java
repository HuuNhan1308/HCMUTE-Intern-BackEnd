package com.intern.app.mapper;

import com.intern.app.models.dto.request.ProfileUpdateRequest;
import com.intern.app.models.dto.request.RecruitmentCreationRequest;
import com.intern.app.models.dto.request.RecruitmentRequestCreationRequest;
import com.intern.app.models.dto.request.RecruitmentRequestGrading;
import com.intern.app.models.dto.response.InstructorRequestResponse;
import com.intern.app.models.dto.response.RecruitmentRequestResponse;
import com.intern.app.models.entity.InstructorRequest;
import com.intern.app.models.entity.Profile;
import com.intern.app.models.entity.Recruitment;
import com.intern.app.models.entity.RecruitmentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RecruitmentRequestMapper {
    RecruitmentRequest toRecruitmentRequest(RecruitmentRequestCreationRequest recruitmentRequestCreationRequest);

    @Mappings({
            @Mapping(target = "recruitment", ignore = true),
            @Mapping(target = "student", ignore = true),
    })
    void updateRecruitmentRequest(@MappingTarget RecruitmentRequest recruitmentRequest, RecruitmentRequestCreationRequest recruitmentRequestCreationRequest);
    void updateRecruitmentRequestWithGrading(@MappingTarget RecruitmentRequest recruitmentRequest, RecruitmentRequestGrading recruitmentRequestGrading);

    RecruitmentRequestResponse toRecruitmentRequestResponse(RecruitmentRequest recruitmentRequest);
}

