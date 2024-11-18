package com.intern.app.models.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
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
    String industry;
    String workingDay;
    String workingHour;
    String businessWebsite;
    String businessImage;

    ProfileResponse managedBy;
}

