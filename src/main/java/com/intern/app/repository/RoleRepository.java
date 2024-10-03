package com.intern.app.repository;

import com.intern.app.models.entity.Role;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends AppRepository<Role, String> {
    Optional<Role> findByRoleName(String roleName);

    Optional<Role> findByRoleIdAndDeletedFalse(String id);
}