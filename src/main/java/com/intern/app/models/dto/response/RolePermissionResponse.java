package com.intern.app.models.dto.response;

import com.intern.app.models.entity.BaseEntity;
import com.intern.app.models.entity.Permission;
import com.intern.app.models.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermissionResponse {
    String id;
    RoleResponse role;
    PermissionResponse permission;
    LocalDateTime dateCreated;
}
