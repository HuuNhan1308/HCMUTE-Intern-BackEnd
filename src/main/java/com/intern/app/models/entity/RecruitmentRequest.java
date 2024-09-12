package com.intern.app.models.entity;

import com.intern.app.models.enums.RecruitmentRequestStatus;
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
    String requestBusinessId;

    RecruitmentRequestStatus businessStatus;
    RecruitmentRequestStatus instructorStatus;
    String messageToBusiness;
    String messageToInstructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StudentId", nullable = false)
    Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RefInstructorId", nullable = true)
    Instructor refInstructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RecruitmentId", nullable = false)
    Recruitment recruitment;
}
