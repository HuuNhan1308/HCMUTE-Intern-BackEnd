package com.intern.app.repository;


import com.intern.app.models.entity.Recruitment;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RecruitmentRepository extends AppRepository<Recruitment, String> {

    Optional<Recruitment> findByTitle(String title);
    Optional<Recruitment> findByRecruitmentIdAndDeletedFalse(String id);
}
