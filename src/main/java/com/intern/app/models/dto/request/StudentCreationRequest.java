package com.intern.app.models.dto.request;

import com.intern.app.models.entity.Business;
import com.intern.app.models.entity.Faculty;
import com.intern.app.models.entity.Profile;
import com.intern.app.models.entity.RecruitmentRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentCreationRequest {
    String studentId;
    Integer year;
    @Builder.Default
    Boolean isSeekingIntern = false;
    Date dob;
    ProfileCreationRequest profile;
}

