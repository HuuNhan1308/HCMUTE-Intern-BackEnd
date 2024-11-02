package com.intern.app.repository;


import com.intern.app.models.entity.Recruitment;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RecruitmentRepository extends AppRepository<Recruitment, String>, JpaSpecificationExecutor<Recruitment> {

    Optional<Recruitment> findByTitle(String title);
    Optional<Recruitment> findByRecruitmentId(String id);

}
