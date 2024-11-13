package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.FacultyMapper;
import com.intern.app.mapper.InstructorMapper;
import com.intern.app.mapper.InstructorRequestMapper;
import com.intern.app.mapper.ProfileMapper;
import com.intern.app.models.dto.datamodel.ExtendPageConfig;
import com.intern.app.models.dto.datamodel.FilterMapping;
import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.request.InstructorCreationRequest;
import com.intern.app.models.dto.request.InstructorRequestCreationRequest;
import com.intern.app.models.dto.request.NotificationRequest;
import com.intern.app.models.dto.response.*;
import com.intern.app.models.entity.*;
import com.intern.app.models.enums.FilterOperator;
import com.intern.app.models.enums.FilterType;
import com.intern.app.models.enums.RequestStatus;
import com.intern.app.repository.*;
import com.intern.app.services.interfaces.IInstructorService;
import com.intern.app.services.interfaces.INotificationService;
import com.intern.app.services.interfaces.IPagingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class InstructorService implements IInstructorService {
    ProfileService profileService;
    RoleRepository roleRepository;
    FacultyRepository facultyRepository;
    InstructorRepository instructorRepository;
    InstructorRequestRepository instructorRequestRepository;
    StudentRepository studentRepository;

    INotificationService notificationService;
    InstructorRequestMapper instructorRequestMapper;
    ProfileRepository profileRepository;
    IPagingService pagingService;
    RecruitmentRequestRepository recruitmentRequestRepository;
    InstructorMapper instructorMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public ReturnResult<Boolean> CreateInstructor(InstructorCreationRequest instructorCreationRequest) {
        var result = new ReturnResult<Boolean>();

        Role instructorRole = roleRepository.findByRoleName("INSTRUCTOR")
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));

        Faculty faculty = facultyRepository.findByFacultyId(instructorCreationRequest.getFacultyId())
                .orElseThrow(() -> new AppException(ErrorCode.FACULTY_NOT_EXISTED));


        Profile createdProfile = profileService.CreateUser(instructorCreationRequest.getProfile(), instructorRole).getResult();
        Instructor instructor = Instructor.builder()
                .faculty(faculty)
                .profile(createdProfile)
                .build();

        instructorRepository.save(instructor);

        result.setResult(Boolean.TRUE);
        result.setCode(200);

        return result;
    }

    @PreAuthorize("hasAuthority('REQUEST_INSTRUCTOR')")
    public ReturnResult<Boolean> RequestInstructor(InstructorRequestCreationRequest instructorRequestCreationRequest) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        Student student = studentRepository.findById(context.getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        Instructor instructor = instructorRepository.findByInstructorId(instructorRequestCreationRequest.getInstructorId())
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));

        if(instructorRequestCreationRequest.getInstructorRequestId() == null) {
            // CASE ADD
            InstructorRequest instructorRequest = instructorRequestRepository
                    .findByStudentStudentIdAndInstructorInstructorIdAndInstructorStatus(
                            student.getStudentId(),
                            instructor.getInstructorId(),
                            RequestStatus.PENDING
                    )
                    .orElse(null);

            if(instructorRequest != null && instructorRequest.getInstructorStatus() == RequestStatus.PENDING) {
                result.setResult(Boolean.FALSE);
                result.setMessage("Bạn đã gửi yêu cầu cho giảng viên này rồi, vui lòng chờ đợi phản hồi");

            }

            List<InstructorRequest> instructorRequests = instructorRequestRepository
                    .findAllByStudentStudentIdAndInstructorStatus(student.getStudentId(), RequestStatus.APPROVED);

            if(!instructorRequests.isEmpty()) {
                result.setResult(Boolean.FALSE);
                result.setMessage("Bạn đã có cho mình giảng viên hướng dẫn, không thể yêu thêm được nữa");
            }

            if(result.getMessage() == null) {
                instructorRequest = InstructorRequest.builder()
                        .student(student)
                        .instructor(instructor)
                        .messageToInstructor(instructorRequestCreationRequest.getMessageToInstructor())
                        .instructorStatus(RequestStatus.PENDING)
                        .build();

                instructorRequestRepository.save(instructorRequest);


                // SEND NOTIFICATION
                NotificationRequest notificationRequest = NotificationRequest.builder()
                        .read(false)
                        .title("Bạn có một yêu cầu đến từ sinh viên")
                        .content("Yêu cầu trở thành giảng viên hướng dẫn đến từ sinh viên " + student.getProfile().getFullname())
                        .ownerId(student.getProfile().getProfileId())
                        .profileId(instructor.getProfile().getProfileId())
                        .build();
                notificationService.SaveNotification(notificationRequest);

                result.setResult(Boolean.TRUE);
            }


        } else {
            // CASE EDIT
            InstructorRequest instructorRequest = instructorRequestRepository
                    .findByInstructorRequestId(instructorRequestCreationRequest.getInstructorRequestId())
                    .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_REQUEST_NOT_FOUND));

            instructorRequestMapper.updateInstructorRequest(instructorRequest, instructorRequestCreationRequest);

            instructorRequestRepository.save(instructorRequest);

            result.setResult(Boolean.TRUE);
        }

        return result;
    }

    @PreAuthorize("hasAuthority('SET_INSTRUCTOR_REQUEST_STATUS')")
    public ReturnResult<Boolean> SetRequestStatus(RequestStatus requestStatus, String instructorRequestId) {
        var result = new ReturnResult<Boolean>();

        InstructorRequest instructorRequest = instructorRequestRepository.findByInstructorRequestId(instructorRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_REQUEST_NOT_FOUND));

        boolean isApproved = instructorRequestRepository.findAllByStudentStudentId(instructorRequest.getStudent().getStudentId())
                .stream()
                .anyMatch((req) -> req.getInstructorStatus().equals(RequestStatus.APPROVED));

        if(isApproved) {
            result.setResult(Boolean.FALSE);
            result.setMessage("Học sinh đã được giảng viên khác chọn, vui lòng tải lại trang");
            result.setCode(200);
        }
        else {
            instructorRequest.setInstructorStatus(requestStatus);

            instructorRequestRepository.save(instructorRequest);

            this.ClearAllStudentAvailableInstructorRequests(instructorRequestId);

            // SEND NOTIFICATION
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .read(false)
                    .title("Yêu cầu giảng viên hướng dẫn đã có kết quả")
                    .content("Đã có kết quả cho yêu cầu giảng viên hướng dẫn " + instructorRequest.getInstructor().getProfile().getFullname())
                    .ownerId(instructorRequest.getInstructor().getProfile().getProfileId())
                    .profileId(instructorRequest.getStudent().getProfile().getProfileId())
                    .build();
            notificationService.SaveNotification(notificationRequest);

            result.setResult(Boolean.TRUE);
            result.setCode(200);
        }

        return result;
    }

    @Override
    public ReturnResult<Boolean> ClearAllStudentAvailableInstructorRequests(String instructorRequestId) {
        var result = new ReturnResult<Boolean>();

        InstructorRequest instructorRequest = instructorRequestRepository.findByInstructorRequestId(instructorRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_REQUEST_NOT_FOUND));

        List<InstructorRequest> pendingInstructorRequests = instructorRequestRepository
                .findAllByStudentStudentIdAndInstructorStatus(instructorRequest.getStudent().getStudentId(), RequestStatus.PENDING);

        pendingInstructorRequests = pendingInstructorRequests.stream()
                .peek(pendingInstructorRequest -> pendingInstructorRequest.setInstructorStatus(RequestStatus.REJECT))
                .toList();

        instructorRequestRepository.saveAll(pendingInstructorRequests);

        result.setResult(Boolean.TRUE);
        result.setCode(200);

        return result;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ReturnResult<PagedData<InstructorRequestResponse, ExtendPageConfig>> GetAllInstructorRequestOfInstructorPaging(ExtendPageConfig pageConfig, String ínstructorId) {
        var result  = new ReturnResult<PagedData<InstructorRequestResponse, ExtendPageConfig>>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Instructor instructor;
        if(profile.getRole().getRoleName().equals("ADMIN")) {
            instructor = instructorRepository.findByInstructorId(ínstructorId)
                    .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));
        } else {
            instructor = profile.getInstructor();
        }

        if(instructor == null) {
            throw new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND);
        }

        // Clone the original PageConfig to keep the original unchanged
        PageConfig customPageConfig = PageConfig.builder()
                .pageSize(pageConfig.getPageSize())
                .currentPage(pageConfig.getCurrentPage())
                .orders(new ArrayList<>(pageConfig.getOrders()))
                .filters(new ArrayList<>(pageConfig.getFilters()))
                .build();

        List<FilterMapping> filterMappings = customPageConfig.getFilters();
        filterMappings.add(FilterMapping.builder()
                .prop("instructor.instructorId")
                .value(instructor.getInstructorId())
                .type(FilterType.TEXT)
                .operator(FilterOperator.EQUALS)
                .build()
        );

        customPageConfig.setFilters(filterMappings);

        var data = pagingService.GetInstructorsRequestPaging(customPageConfig).getResult();

        // Additional data
        data.setData(data.getData().stream().peek(instructorRequestResponse -> {
            RecruitmentRequest recruitmentRequest = recruitmentRequestRepository
                    .findByStudentStudentIdAndBusinessStatus(instructorRequestResponse.getStudent().getStudentId(), RequestStatus.APPROVED)
                    .orElse(null);

            if(recruitmentRequest != null) {
                instructorRequestResponse.setRecruitmentId(recruitmentRequest.getRecruitment().getRecruitmentId());
                instructorRequestResponse.setRecruitmentTitle(recruitmentRequest.getRecruitment().getTitle());
            }

            instructorRequestResponse.setInstructor(null);
        }).toList());

        // Set data for page
        ExtendPageConfig pageConfigResult = ExtendPageConfig
                .builder()
                .pageSize(data.getPageConfig().getPageSize())
                .totalRecords(data.getPageConfig().getTotalRecords())
                .totalPage(data.getPageConfig().getTotalPage())
                .currentPage(data.getPageConfig().getCurrentPage())
                .orders(pageConfig.getOrders())
                .filters(pageConfig.getFilters())
                .build();

        // Set Additional Data for page
        var numberOfApprovedStudent = instructorRequestRepository.findAllByInstructorInstructorIdAndInstructorStatus(instructor.getInstructorId(), RequestStatus.APPROVED).size();
        pageConfigResult.setAdditionalData(Map.of("numberOfApprovedStudent", numberOfApprovedStudent));

        // Build the PagedData object
        result.setResult(
                PagedData.<InstructorRequestResponse, ExtendPageConfig>builder()
                        .data(data.getData())
                        .pageConfig(pageConfigResult)
                        .build()
        );

        return result;
    }

    public ReturnResult<InstructorResponse> GetInstructorData(String instructorId) {
        var result = new ReturnResult<InstructorResponse>();

        Instructor instructor = instructorRepository.findByInstructorId(instructorId).orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));
        InstructorResponse instructorResponse = instructorMapper.toInstructorResponse(instructor);

        result.setResult(instructorResponse);
        result.setCode(200);

        return result;
    }

    public ReturnResult<InstructorResponse> GetMyInstructorData() {
        var result = new ReturnResult<InstructorResponse>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Instructor instructor = profile.getInstructor();

        InstructorResponse instructorResponse = instructorMapper.toInstructorResponse(instructor);

        result.setResult(instructorResponse);
        result.setCode(200);

        return result;
    }
}
