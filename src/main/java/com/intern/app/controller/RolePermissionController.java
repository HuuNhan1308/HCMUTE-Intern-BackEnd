package com.intern.app.controller;

import com.github.javafaker.Bool;
import com.intern.app.exception.AppException;
import com.intern.app.models.dto.request.PermissionCreationRequest;
import com.intern.app.models.dto.request.RolePermissionCreationRequest;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.services.RolePermissionService;
import com.intern.app.services.UploadService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/rolepermission")
@AllArgsConstructor
public class RolePermissionController {

    RolePermissionService rolePermissionService;

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
