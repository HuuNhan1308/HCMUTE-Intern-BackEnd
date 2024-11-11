package com.intern.app.models.dto.request;

import com.intern.app.models.entity.*;
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
    Boolean isSeekingIntern = true;
    Date dob;
    ProfileCreationRequest profile;
}

