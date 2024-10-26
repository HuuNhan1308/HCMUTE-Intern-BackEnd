package com.intern.app.models.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Instructor extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String instructorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FacultyId")
    Faculty faculty;

    @OneToOne()
    @JoinColumn(name = "ProfileId", referencedColumnName = "profileId",nullable = false)
    Profile profile;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "refInstructor")
    List<Business> recommendedBusinesses;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "instructor")
    List<InstructorRequest> instructorRequests;
}
