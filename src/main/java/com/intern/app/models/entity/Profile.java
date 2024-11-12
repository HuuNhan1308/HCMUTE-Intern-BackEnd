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

    String fullname;
    Boolean isMale;
    @Column(columnDefinition = "TEXT")
    String bio;
    @Column(unique = true, nullable = false)
    String username;
    @Column(nullable = false)
    String password;
    String phoneNumber;
    String email;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "profile")
    UploadContent uploadContent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    List<ProfilePermission> profilePermissions;

    @OneToOne(mappedBy = "profile")
    Student student;

    @OneToOne(mappedBy = "profile")
    Instructor instructor;

    @OneToOne(mappedBy = "managedBy", cascade = CascadeType.ALL)
    Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    List<Notification> ownedNotifications;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    List<Notification> receivedNotifications;
}
