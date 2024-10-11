package com.intern.app.models.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BusinessResponse {
    String businessId;

    String name;
    String overview;
    String location;
    String type;
    String industry;
    String workingDay;
    String workingHour;

    ProfileResponse managedBy;
}
