package com.intern.app.mapper;

import com.intern.app.models.dto.request.RecruitmentCreationRequest;
import com.intern.app.models.dto.request.RecruitmentUpdateRequest;
import com.intern.app.models.dto.response.RecruitmentResponse;
import com.intern.app.models.dto.response.RecruitmentResponseShort;
import com.intern.app.models.entity.Recruitment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RecruitmentMapper {
    Recruitment toRecruitment(RecruitmentCreationRequest recruitmentCreationRequest);

    RecruitmentResponse toRecruitmentResponse(Recruitment recruitment);

    @Mappings({
            @Mapping(target = "businessName", source = "business.name"),
            @Mapping(target = "businessImage", source = "business.managedBy.profileId")
    })
    RecruitmentResponseShort toRecruitmentResponseShort(Recruitment recruitment);

    @Mappings({
            @Mapping(target = "business", ignore = true),
            @Mapping(target = "recruitmentId", ignore = true),
            @Mapping(target = "recruitmentRequests", ignore = true),
    })
    void updateRecruitment(@MappingTarget Recruitment recruitment, RecruitmentUpdateRequest recruitmentUpdateRequest);
}
