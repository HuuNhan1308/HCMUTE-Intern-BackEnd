package com.intern.app.repository;


import com.intern.app.models.entity.Recruitment;
import com.intern.app.models.entity.RecruitmentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RecruitmentRequestRepository extends JpaRepository<RecruitmentRequest, String> {

}
