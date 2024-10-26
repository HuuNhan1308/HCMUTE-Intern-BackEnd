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
public class InstructorRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String instructorRequestId;

    RequestStatus instructorStatus;
    String messageToInstructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StudentId", nullable = false)
    Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "InstructorId")
    Instructor instructor;
}
