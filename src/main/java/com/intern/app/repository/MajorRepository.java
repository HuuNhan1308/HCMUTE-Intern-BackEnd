package com.intern.app.repository;

import com.intern.app.models.entity.Faculty;
import com.intern.app.models.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MajorRepository extends JpaRepository<Major, String> {

    Optional<Major> findByName(String name);

    Optional<Major> findByNameAndFaculty(String name, Faculty faculty);

    List<Major> findByFaculty(Faculty faculty);
}
