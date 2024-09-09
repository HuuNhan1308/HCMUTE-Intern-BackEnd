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
public class Role extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String RoleId;

    @Column(nullable = false)
    String RoleName;

}
