package com.intern.app.models.dto.response;

import com.intern.app.models.dto.request.ProfileCreationRequest;
import com.intern.app.models.entity.Business;
import com.intern.app.models.entity.Faculty;
import com.intern.app.models.entity.InstructorRequest;
import com.intern.app.models.entity.Profile;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InstructorResponse {
    FacultyResponse faculty;
    ProfileResponse profile;

    String instructorId;

    List<BusinessResponse> recommendedBusinesses;

    List<InstructorRequestResponse> instructorRequests;
}
