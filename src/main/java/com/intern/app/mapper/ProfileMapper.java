package com.intern.app.mapper;

import com.intern.app.models.dto.request.ProfileCreationRequest;
import com.intern.app.models.dto.response.ProfileResponse;
import com.intern.app.models.entity.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(ProfileCreationRequest profileCreationRequest);
    ProfileResponse toProfileResponse(Profile profile);
}
