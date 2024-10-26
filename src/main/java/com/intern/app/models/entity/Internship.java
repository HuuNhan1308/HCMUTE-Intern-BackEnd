package com.intern.app.models.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Internship extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String internshipId;

    Date startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StudentId", nullable = false)
    Student student;
}
