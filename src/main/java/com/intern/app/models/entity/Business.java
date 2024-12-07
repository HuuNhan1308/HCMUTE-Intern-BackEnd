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
public class Business extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String businessId;

    String name;
    @Column(length = 5000)
    String overview;
    String location;
    String industry;
    String workingDay;
    String workingHour;
    String businessWebsite;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "managed_by", nullable = false)
    Profile managedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ref_instructor_id")
    Instructor refInstructor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "business")
    List<Recruitment> recruitments;

}

