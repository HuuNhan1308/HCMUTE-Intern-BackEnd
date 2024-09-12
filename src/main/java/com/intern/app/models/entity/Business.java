package com.intern.app.models.entity;

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
public class Business extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String businessId;

    String name;
    String overview;
    String location;
    String type;
    String industry;
    String workingDay;
    String workingHour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ManagedBy")
    Profile managedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RefInstructorId")
    Instructor refInstructor;
}
