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
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_seq")
    @SequenceGenerator(name = "permission_seq", sequenceName = "permission_sequence", initialValue = 1000, allocationSize = 1)
    Integer permissionId;

    String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "permission")
    List<RolePermission> rolePermissions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "permission")
    List<ProfilePermission> profilePermissions;
}

