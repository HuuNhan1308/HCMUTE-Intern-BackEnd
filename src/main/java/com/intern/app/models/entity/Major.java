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
public class Major extends BaseEntity {
    @Id
    String majorId;

    String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "major")
    List<Student> students;
}