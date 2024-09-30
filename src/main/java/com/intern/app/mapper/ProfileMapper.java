package com.intern.app.mapper;

import com.intern.app.models.dto.request.ProfileCreationRequest;
import com.intern.app.models.dto.request.ProfileUpdateRequest;
import com.intern.app.models.dto.response.ProfileResponse;
import com.intern.app.models.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(ProfileCreationRequest profileCreationRequest);
    ProfileResponse toProfileResponse(Profile profile);

    @Mappings({
            @Mapping(target = "student", ignore = true),
            @Mapping(target = "instructor", ignore = true),
            @Mapping(target = "business", ignore = true),
            @Mapping(target = "profileId", ignore = true)
    })
    void updateProfile(@MappingTarget Profile profile, ProfileUpdateRequest profileUpdateRequest);
}
