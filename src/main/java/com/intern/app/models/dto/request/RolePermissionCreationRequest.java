package com.intern.app.models.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermissionCreationRequest {
    List<String> roleIds;
    List<Integer> permissionIds;
}
