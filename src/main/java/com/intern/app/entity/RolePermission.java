package com.intern.app.entity;

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
@Table(name = "role_permission")
public class RolePermission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String UploadContentId;
}
