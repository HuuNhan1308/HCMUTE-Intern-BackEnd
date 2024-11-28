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
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String roleId;

    @Column(nullable = false)
    String roleName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    List<RolePermission> rolePermissions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    List<Profile> profiles;
}

