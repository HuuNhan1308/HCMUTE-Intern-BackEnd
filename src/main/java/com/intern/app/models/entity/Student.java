package com.intern.app.models.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student extends BaseEntity {
    @Id
    String studentId;
    Integer year;
    @Builder.Default
    Boolean isSeekingIntern = true;

    @Temporal(TemporalType.DATE)
    Date dob;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    Major major;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id")
    Faculty faculty;

    @OneToOne()
    @JoinColumn(name = "profile_id",nullable = false)
    Profile profile;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    List<RecruitmentRequest> recruitmentRequests;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    List<InstructorRequest> instructorRequest;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    List<Internship> internships;
}
