package com.intern.app.mapper;

import com.intern.app.models.dto.request.BusinessCreationRequest;
import com.intern.app.models.dto.response.BusinessResponse;
import com.intern.app.models.dto.response.FacultyResponse;
import com.intern.app.models.entity.Business;
import com.intern.app.models.entity.Faculty;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessMapper {
//    FacultyResponse toBusinessrespo(Faculty faculty);

    Business toBusiness(BusinessCreationRequest businessCreationRequest);
    BusinessResponse toBusinessResponse(Business business);

//    void updateBusiness(Business business, BusinessCreationRequest businessCreationRequest);
}