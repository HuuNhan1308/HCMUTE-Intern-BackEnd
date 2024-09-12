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
@Table(name = "profile_permission")
public class ProfilePermission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String Id;

    @ManyToOne()
    @JoinColumn(name = "profileId")
    Profile profile;

    @ManyToOne()
    @JoinColumn(name = "permissionId")
    Permission permission;
}
