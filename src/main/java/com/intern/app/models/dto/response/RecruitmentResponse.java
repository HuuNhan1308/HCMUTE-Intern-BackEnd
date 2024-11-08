package com.intern.app.models.dto.response;

import com.intern.app.models.entity.Business;
import com.intern.app.models.entity.Recruitment;
import com.intern.app.models.entity.Student;
import com.intern.app.models.enums.RecruitmentStatus;
import com.intern.app.models.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecruitmentResponse {
    String recruitmentId;
    String title;
    String description;
    String location;
    String type;
    String workingDay;
    String workingHour;
    String keySkills;
    String position;
    RecruitmentStatus status;

    BusinessResponse business;
}
