package com.intern.app.repository;

import com.intern.app.models.entity.Instructor;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface InstructorRepository extends AppRepository<Instructor, String>, JpaSpecificationExecutor<Instructor> {
    Optional<Instructor> findByInstructorId(String id);
}
