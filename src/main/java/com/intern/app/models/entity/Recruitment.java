package com.intern.app.models.entity;

import com.intern.app.models.enums.RecruitmentStatus;
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
public class Recruitment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String recruitmentId;

    String title;
    @Column(length = 5000)
    String description;
    String location;
    String type;
    String workingDay;
    String workingHour;
    @Column(length = 5000)
    String keySkills;
    String position;

    @Builder.Default
    RecruitmentStatus status = RecruitmentStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    Business business;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recruitment")
    List<RecruitmentRequest> recruitmentRequests;
}
