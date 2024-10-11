package com.intern.app.models.dto.response;

import com.intern.app.models.dto.request.ProfileCreationRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InstructorResponse {
    FacultyResponse faculty;
    ProfileResponse profile;
}
