package com.intern.app.repository;

import com.intern.app.models.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, String> {
    Optional<Faculty> findByName(String name);
    Optional<Faculty> findByFacultyId(String facultyId);
}
