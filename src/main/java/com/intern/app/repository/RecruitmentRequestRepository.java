package com.intern.app.repository;


import com.intern.app.models.entity.Recruitment;
import com.intern.app.models.entity.RecruitmentRequest;
import com.intern.app.models.entity.Student;
import com.intern.app.models.enums.RequestStatus;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitmentRequestRepository extends AppRepository<RecruitmentRequest, String>, JpaSpecificationExecutor<RecruitmentRequest> {
    Optional<RecruitmentRequest> findByRecruitmentRequestId(String recruitmentId);

    @Query("SELECT rr FROM RecruitmentRequest rr WHERE rr.student = :student AND rr.deleted = false AND rr.businessStatus = :status")
    List<RecruitmentRequest> findByStudentAndStatus(Student student, RequestStatus status);

    Optional<RecruitmentRequest> findByStudentStudentIdAndBusinessStatus(String studentId, RequestStatus status);

    List<RecruitmentRequest> findByRecruitmentAndBusinessStatus(Recruitment recruitment, RequestStatus status);

    Optional<RecruitmentRequest> findByStudentStudentIdAndRecruitmentRecruitmentIdAndBusinessStatus(String studentId, String recruitmentId, RequestStatus status);

    List<RecruitmentRequest> findAllByStudentStudentId(String studentId);
}
