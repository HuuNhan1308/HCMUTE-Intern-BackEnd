package com.intern.app.controller;

import com.intern.app.exception.AppException;
import com.intern.app.models.dto.request.PermissionCreationRequest;
import com.intern.app.models.dto.request.RolePermissionCreationRequest;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.services.implement.RolePermissionService;
import com.intern.app.services.interfaces.IRolePermissionService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/rolepermission")
@AllArgsConstructor
public class RolePermissionController {
    IRolePermissionService rolePermissionService;

    @PostMapping("/CreatePermission")
    public ResponseEntity<ReturnResult<Boolean>> CreatePermission(@RequestBody PermissionCreationRequest permissionCreationRequest) throws AppException {
        ReturnResult<Boolean> result = rolePermissionService.CreatePermission(permissionCreationRequest);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/BindRolePermission")
    public ResponseEntity<ReturnResult<Boolean>> BindRolePermission(@RequestBody RolePermissionCreationRequest rolePermissionCreationRequest) throws AppException {
        ReturnResult<Boolean> result = rolePermissionService.BindRolePermission(rolePermissionCreationRequest);

        return ResponseEntity.ok().body(result);
    }

}
