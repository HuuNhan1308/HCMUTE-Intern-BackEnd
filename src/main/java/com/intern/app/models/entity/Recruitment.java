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
public class Recruitment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String recruitmentId;

    String title;
    @Column(length = 1000)
    String description;
    String location;
    String type;
    String workingDay;
    String workingHour;
    String keySkills;
    String position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BusinessId")
    Business business;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recruitment")
    List<RecruitmentRequest> recruitmentRequests;
}
