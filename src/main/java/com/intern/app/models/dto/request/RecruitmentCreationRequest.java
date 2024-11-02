package com.intern.app.models.dto.request;

import com.intern.app.models.enums.RecruitmentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecruitmentCreationRequest {
    String title;
    String description;
    String location;
    String type;
    String workingDay;
    String workingHour;
    String keySkills;
    String position;
}

