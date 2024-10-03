package com.intern.app.services;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.RecruitmentMapper;
import com.intern.app.mapper.RecruitmentRequestMapper;
import com.intern.app.models.dto.request.RecruitmentCreationRequest;
import com.intern.app.models.dto.request.RecruitmentRequestCreationRequest;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.*;
import com.intern.app.repository.InstructorRepository;
import com.intern.app.repository.ProfileRepository;
import com.intern.app.repository.RecruitmentRepository;
import com.intern.app.repository.StudentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@EnableMethodSecurity
public class RecruitmentService {
    RecruitmentMapper recruitmentMapper;
    RecruitmentRequestMapper recruitmentRequestMapper;


    ProfileRepository profileRepository;
    RecruitmentRepository recruitmentRepository;
    StudentRepository studentRepository;
    InstructorRepository instructorRepository;

    @PreAuthorize("hasRole('BUSINESS')")
    public ReturnResult<Boolean> CreateRecruitment(RecruitmentCreationRequest recruitmentCreationRequest) {
        var result = new ReturnResult<Boolean>();

        Recruitment recruitment = recruitmentMapper.toRecruitment(recruitmentCreationRequest);

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Optional<Profile> profile = profileRepository.findByUsernameAndDeletedFalse(username);

        if(profile.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        if(profile.get().getBusiness() == null) {
            throw new AppException(ErrorCode.BUSINESS_NOT_FOUND);
        }

        recruitment.setBusiness(profile.get().getBusiness());

        Recruitment createdRecruitment = recruitmentRepository.save(recruitment);

        result.setCode(200);
        result.setResult(createdRecruitment != null);

        return result;
    }

    @PreAuthorize("hasAuthority('REQUEST_RECRUITMENT')")
    public ReturnResult<Boolean> RequestRecruitment(RecruitmentRequestCreationRequest recruitmentRequestCreationRequest) {
        var result = new ReturnResult<Boolean>();

        RecruitmentRequest recruitmentRequest = recruitmentRequestMapper.toRecruitmentRequest(recruitmentRequestCreationRequest);

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsernameAndDeletedFalse(username).orElse(null);
        Recruitment recruitment = recruitmentRepository.findByRecruitmentIdAndDeletedFalse(recruitmentRequestCreationRequest.getRecruitmentId()).orElse(null);
        Instructor instructor = instructorRepository.findByInstructorIdAndDeletedFalse(recruitmentRequestCreationRequest.getInstructorId()).orElse(null);

        if(profile == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        if(profile.getStudent() == null) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }

        if(recruitment == null) {
            throw new AppException(ErrorCode.RECRUITMENT_NOT_FOUND);
        }

        recruitmentRequest.setStudent(profile.getStudent());
        recruitmentRequest.setRecruitment(recruitment);
        recruitmentRequest.setRefInstructor(instructor);


        return result;
    }
}
