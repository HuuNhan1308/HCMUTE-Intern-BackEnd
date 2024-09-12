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
public class Profile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String profileId;

    String firstName;
    String lastName;
    String gender;
    String bio;
    @Column(unique = true, nullable = false)
    String username;
    @Column(nullable = false)
    String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    List<UploadContent> uploadContents;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    List<ProfilePermission> profilePermissions;

    @OneToOne(mappedBy = "profile")
    Student student;
}
