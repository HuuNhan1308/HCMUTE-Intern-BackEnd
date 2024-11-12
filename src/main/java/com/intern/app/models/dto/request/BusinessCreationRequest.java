package com.intern.app.models.dto.request;

import com.intern.app.models.entity.Instructor;
import com.intern.app.models.entity.Profile;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BusinessCreationRequest {
    String name;
    String overview;
    String location;
    String industry;
    String workingDay;
    String workingHour;
    String businessWebsite;

    ProfileCreationRequest managedBy;

    String instructorId;
}

