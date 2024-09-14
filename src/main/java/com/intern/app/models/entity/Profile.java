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
    @Column(columnDefinition = "TEXT")
    String bio;
    @Column(unique = true, nullable = false)
    String username;
    @Column(nullable = false)
    String password;
    String phoneNumber;
    String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    List<UploadContent> uploadContents;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    List<ProfilePermission> profilePermissions;

    @OneToOne(mappedBy = "profile")
    Student student;

    @OneToOne(mappedBy = "profile")
    Instructor instructor;

    @OneToOne(mappedBy = "managedBy")
    Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
    Role role;
}
