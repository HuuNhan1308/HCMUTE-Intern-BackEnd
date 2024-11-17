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
public class BusinessWithRecruitmentsResponse extends BusinessResponse {
    List<RecruitmentResponse> recruitments;
}
