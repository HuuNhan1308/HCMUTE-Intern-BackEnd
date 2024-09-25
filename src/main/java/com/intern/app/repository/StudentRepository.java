package com.intern.app.repository;

import com.intern.app.models.entity.Profile;
import com.intern.app.models.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
    Optional<Student> findByProfile(Profile profile);

    @Query(value = "SELECT s.* FROM Student s JOIN Profile p ON s.profile_id = p.profile_id " +
            "JOIN Major m ON s.major_id = m.major_id " +
            "WHERE LOWER(p.fullname) LIKE LOWER(CONCAT('%', :fullname, '%')) " +
            "AND m.major_id IN :majorIds",
            nativeQuery = true)
    Page<Student> findByFullnameContainingIgnoreCaseAndMajorIdIn(@Param("fullname") String fullname, @Param("majorIds") List<String> majorIds, Pageable pageable);

    @Query(value = "SELECT s.* FROM Student s JOIN Profile p ON s.profile_id = p.profile_id " +
            "WHERE LOWER(p.fullname) LIKE LOWER(CONCAT('%', :fullname, '%'))",
            nativeQuery = true)
    Page<Student> findByFullnameContainingIgnoreCase(@Param("fullname") String fullname, Pageable pageable);


}
