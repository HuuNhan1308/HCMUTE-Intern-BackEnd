package com.intern.app.repository;

import com.intern.app.models.entity.Faculty;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyRepository extends AppRepository<Faculty, String> {
    Optional<Faculty> findByName(String name);
    Optional<Faculty> findByFacultyId(String facultyId);
}
