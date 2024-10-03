package com.intern.app.repository;


import com.intern.app.models.entity.RecruitmentRequest;
import com.intern.app.models.entity.Student;
import com.intern.app.models.enums.RecruitmentRequestStatus;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitmentRequestRepository extends AppRepository<RecruitmentRequest, String> {
    Optional<RecruitmentRequest> findByRecruitmentRequestIdAndDeletedFalse(String recruitmentId);

    @Query("SELECT rr FROM RecruitmentRequest rr WHERE rr.student = :student AND rr.deleted = false AND rr.businessStatus = :status")
    List<RecruitmentRequest> findByStudentAndStatus(Student student, RecruitmentRequestStatus status);
}
