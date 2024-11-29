package com.intern.app.repository;

import com.intern.app.models.entity.Role;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.apache.el.parser.JJTELParserState;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends AppRepository<Role, String>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByRoleName(String roleName);

    Optional<Role> findByRoleIdAndDeletedFalse(String id);
}