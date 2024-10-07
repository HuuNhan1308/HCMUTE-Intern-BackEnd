package com.intern.app.repository;

import com.intern.app.models.entity.Faculty;
import com.intern.app.models.entity.Major;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MajorRepository extends AppRepository<Major, String> {

    Optional<Major> findByName(String name);

    Optional<Major> findByNameAndFaculty(String name, Faculty faculty);

    List<Major> findByFaculty(Faculty faculty);
}
