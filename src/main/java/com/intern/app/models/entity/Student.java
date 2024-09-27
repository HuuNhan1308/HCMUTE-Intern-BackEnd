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
    Boolean isSeekingIntern = false;

    @Temporal(TemporalType.DATE)
    Date dob;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MajorId")
    Major major;

    @OneToOne()
    @JoinColumn(name = "ProfileId", referencedColumnName = "profileId",nullable = false)
    Profile profile;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    List<RecruitmentRequest> recruitmentRequests;
}
