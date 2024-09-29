package com.intern.app.repository;

import com.intern.app.models.entity.Faculty;
import com.intern.app.models.entity.Major;
import com.intern.app.models.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, String> {

    Optional<Recruitment> findByTitle(String title);
}
