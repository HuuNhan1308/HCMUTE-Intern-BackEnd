package com.intern.app.mapper;

import com.intern.app.dto.request.ProfileCreationRequest;
import com.intern.app.dto.response.ProfileResponse;
import com.intern.app.entity.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(ProfileCreationRequest profileCreationRequest);

    ProfileResponse toProfileResponse(Profile profile);
}
