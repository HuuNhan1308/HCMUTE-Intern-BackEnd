package com.intern.app.repository;

import com.intern.app.models.entity.Profile;
import com.intern.app.models.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
    Optional<Student> findByProfile(Profile profile);

    @Query(value = "SELECT s.* FROM Student s JOIN Profile p ON s.profile_id = p.profile_id JOIN Major m ON s.major_id = m.major_id WHERE p.fullname LIKE %:fullname% AND m.major_id = :majorId", nativeQuery = true)
    Page<Student> findByFullnameContainingAndMajorId(@Param("fullname") String fullname, @Param("majorId") String majorId, Pageable pageable);
    
    @Query(value = "SELECT s.* FROM Student s JOIN Profile p ON s.profile_id = p.profile_id WHERE p.fullname LIKE %:fullname%", nativeQuery = true)
    Page<Student> findByFullnameContaining(@Param("fullname") String fullname, Pageable pageable);
}
