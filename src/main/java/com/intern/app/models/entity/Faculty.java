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
public class Faculty extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String facultyId;
    String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "faculty")
    List<Student> students;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "faculty")
    List<Instructor> instructors;
}