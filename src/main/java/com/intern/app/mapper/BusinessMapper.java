package com.intern.app.mapper;

import com.intern.app.models.dto.request.BusinessCreationRequest;
import com.intern.app.models.dto.request.BusinessUpdateRequest;
import com.intern.app.models.dto.response.BusinessResponse;
import com.intern.app.models.dto.response.BusinessWithRecruitmentsResponse;
import com.intern.app.models.entity.Business;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface BusinessMapper {

    Business toBusiness(BusinessCreationRequest businessCreationRequest);

    @Mapping(target = "businessImage", source = "managedBy.profileId")
    BusinessResponse toBusinessResponse(Business business);

    @Mappings({
            @Mapping(target = "recruitments", ignore = true)
    })
    BusinessWithRecruitmentsResponse toBusinessResponseWithRecruitments(Business business);

    @Mappings({
            @Mapping(target = "businessId", ignore = true),
            @Mapping(target = "managedBy", ignore = true),
            @Mapping(target = "refInstructor", ignore = true),
    })
    void updateBusiness(@MappingTarget Business business, BusinessUpdateRequest businessCreationRequest);
}
