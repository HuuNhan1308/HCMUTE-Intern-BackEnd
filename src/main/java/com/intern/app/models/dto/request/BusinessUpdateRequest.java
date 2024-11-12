package com.intern.app.models.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BusinessUpdateRequest {
    String name;
    String overview;
    String location;
    String industry;
    String workingDay;
    String workingHour;
    String businessWebsite;

    ProfileUpdateRequest managedBy;
}
