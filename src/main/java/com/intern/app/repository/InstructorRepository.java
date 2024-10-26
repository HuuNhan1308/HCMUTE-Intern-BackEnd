package com.intern.app.repository;

import com.intern.app.models.entity.Instructor;
import com.intern.app.repository.CustomRepository.AppRepository;

import java.util.Optional;

public interface InstructorRepository extends AppRepository<Instructor, String> {
    Optional<Instructor> findByInstructorId(String id);
}
