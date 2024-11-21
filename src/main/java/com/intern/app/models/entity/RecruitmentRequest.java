package com.intern.app.models.entity;

import com.intern.app.models.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecruitmentRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String recruitmentRequestId;

    RequestStatus businessStatus;
    String messageToBusiness;
    Double point;
    String messageToStudent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id", nullable = false)
    Recruitment recruitment;
}

