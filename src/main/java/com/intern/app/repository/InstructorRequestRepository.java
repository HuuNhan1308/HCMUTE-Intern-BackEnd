package com.intern.app.repository;


import com.intern.app.models.entity.Instructor;
import com.intern.app.models.entity.InstructorRequest;
import com.intern.app.models.enums.RequestStatus;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRequestRepository extends AppRepository<InstructorRequest, String>, JpaSpecificationExecutor<InstructorRequest> {
    Optional<InstructorRequest> findByStudentStudentIdAndInstructorInstructorId(String studentId, Instructor instructor);
    Optional<InstructorRequest> findByInstructorRequestId(String instructorRequestId);

    List<InstructorRequest> findAllByStudentStudentId(String studentId);

    List<InstructorRequest> findAllByStudentStudentIdAndInstructorStatus(String studentId, RequestStatus status);
}
