package com.intern.app.controller;

import com.intern.app.exception.AppException;
import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.request.PermissionCreationRequest;
import com.intern.app.models.dto.request.RolePermissionCreationRequest;
import com.intern.app.models.dto.response.PermissionResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.dto.response.RolePermissionResponse;
import com.intern.app.models.dto.response.RoleResponse;
import com.intern.app.models.entity.RolePermission;
import com.intern.app.services.implement.PagingService;
import com.intern.app.services.implement.RolePermissionService;
import com.intern.app.services.interfaces.IRolePermissionService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/rolepermission")
@AllArgsConstructor
public class RolePermissionController {
    private final PagingService pagingService;
    IRolePermissionService rolePermissionService;

    @PostMapping("/CreatePermission")
    public ResponseEntity<ReturnResult<Boolean>> CreatePermission(@RequestBody PermissionCreationRequest permissionCreationRequest) {
        ReturnResult<Boolean> result = rolePermissionService.CreatePermission(permissionCreationRequest);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/BindRolePermission")
    public ResponseEntity<ReturnResult<Boolean>> BindRolePermission(@RequestBody RolePermissionCreationRequest rolePermissionCreationRequest) {
        ReturnResult<Boolean> result = rolePermissionService.BindRolePermission(rolePermissionCreationRequest);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/GetRolePermissionPaging")
    public ResponseEntity<ReturnResult<PagedData<RolePermissionResponse, PageConfig>> > GetRolePermissionPaging(@RequestBody PageConfig pageConfig) {
        ReturnResult<PagedData<RolePermissionResponse, PageConfig>> result = pagingService.GetRolePermissionPaging(pageConfig);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/GetAllRole")
    public ResponseEntity<ReturnResult<List<RoleResponse>>> GetAllRole() {
        ReturnResult<List<RoleResponse>> result = rolePermissionService.GetAllRole();

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/GetAllPermission")
    public ResponseEntity<ReturnResult<List<PermissionResponse>>> GetAllPermission() {
        ReturnResult<List<PermissionResponse>> result = rolePermissionService.GetAllPermission();

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/GetPermissionByRoleId")
    public ResponseEntity<ReturnResult<List<PermissionResponse>>> GetPermissionByRoleId(@RequestParam String roleId) {
        ReturnResult<List<PermissionResponse>> result = rolePermissionService.GetPermissionByRoleId(roleId);

        return ResponseEntity.ok().body(result);
    }
}
