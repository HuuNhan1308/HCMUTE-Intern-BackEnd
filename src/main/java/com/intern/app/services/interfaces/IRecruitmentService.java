package com.intern.app.services.interfaces;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.models.dto.request.RecruitmentCreationRequest;
import com.intern.app.models.dto.request.RecruitmentRequestCreationRequest;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.*;
import com.intern.app.models.enums.RecruitmentRequestStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

public interface IRecruitmentService {
    ReturnResult<Boolean> CreateRecruitment(RecruitmentCreationRequest recruitmentCreationRequest);

    ReturnResult<Boolean> RequestRecruitment(RecruitmentRequestCreationRequest recruitmentRequestCreationRequest);

    ReturnResult<Boolean> ClearAllStudentAvailableRecruitmentRequests(Student student);
}
