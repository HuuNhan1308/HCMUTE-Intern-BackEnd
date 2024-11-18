package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.*;
import com.intern.app.models.dto.datamodel.FilterSpecification;
import com.intern.app.models.dto.datamodel.PageConfig;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.response.*;
import com.intern.app.models.entity.*;
import com.intern.app.models.enums.RecruitmentStatus;
import com.intern.app.repository.*;
import com.intern.app.services.interfaces.IAvatarService;
import com.intern.app.services.interfaces.IPagingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PagingService implements IPagingService {

    InstructorRequestRepository instructorRequestRepository;
    StudentRepository studentRepository;
    RecruitmentRepository recruitmentRepository;
    RecruitmentRequestRepository recruitmentRequestRepository;
    BusinessRepository businessRepository;
    FacultyRepository facultyRepository;
    NotificationRepository notificationRepository;

    InstructorRequestMapper instructorRequestMapper;
    StudentMapper studentMapper;
    ProfileMapper profileMapper;
    InstructorMapper instructorMapper;
    RecruitmentMapper recruitmentMapper;
    RecruitmentRequestMapper recruitmentRequestMapper;
    InstructorRepository instructorRepository;
    FacultyMapper facultyMapper;
    BusinessMapper businessMapper;
    NotificationMapper notificationMapper;
    IAvatarService avatarService;

    public ReturnResult<PagedData<InstructorRequestResponse, PageConfig>> GetInstructorsRequestPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<InstructorRequestResponse, PageConfig>>();

        //Get page config filter spec
        FilterSpecification<InstructorRequest> filter = new FilterSpecification<InstructorRequest>();
        Specification<InstructorRequest> instructorRequestSpecification = filter.GetSearchSpecification(pageConfig.getFilters());

        //Get sort
        Sort sort = pageConfig.getSortAndNewItem();

        List<InstructorRequest> instructorRequests;
        Page<InstructorRequest> instructorRequestPage = null;
        if (pageConfig.getPageSize() == -1) {
            // Fetch all elements when pageSize is -1
            pageConfig.setCurrentPage(1);
            instructorRequests = instructorRequestRepository.findAll(instructorRequestSpecification, sort);
        } else {
            // Fetch paginated data
            Pageable pageable = PageRequest.of(pageConfig.getCurrentPage() - 1, pageConfig.getPageSize(), sort);
            instructorRequestPage = instructorRequestRepository.findAll(instructorRequestSpecification, pageable);
            instructorRequests = instructorRequestPage.getContent();
        }

        // transform data to response DTO
        List<InstructorRequestResponse> instructorRequestResponses = instructorRequests.stream().map(instructorRequest -> {
            InstructorRequestResponse instructorRequestResponse = instructorRequestMapper.toInstructorRequestResponse(instructorRequest);
            instructorRequestResponse.setInstructor(instructorMapper.toInstructorResponse(instructorRequest.getInstructor()));
            instructorRequestResponse.setStudent(studentMapper.toStudentResponse(instructorRequest.getStudent()));

            return instructorRequestResponse;
        }).toList();


        // Set data for page
        PageConfig pageConfigResult;
        if (pageConfig.getPageSize() == -1) {
            pageConfigResult = PageConfig.builder()
                    .pageSize(instructorRequests.size())
                    .totalRecords(instructorRequests.size())
                    .totalPage(1)
                    .currentPage(1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();
        } else {
            if (instructorRequestPage == null) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);

            pageConfigResult = PageConfig
                    .builder()
                    .pageSize(instructorRequestPage.getSize())
                    .totalRecords((int) instructorRequestPage.getTotalElements())
                    .totalPage(instructorRequestPage.getTotalPages())
                    .currentPage(instructorRequestPage.getNumber() + 1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();
        }

        // Build the PagedData object
        result.setResult(
                PagedData.<InstructorRequestResponse, PageConfig>builder()
                        .data(instructorRequestResponses)
                        .pageConfig(pageConfigResult)
                        .build()
        );

        result.setCode(HttpStatus.OK.value());
        return result;
    }

    public ReturnResult<PagedData<StudentResponse, PageConfig>> GetStudentPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<StudentResponse, PageConfig>>();

        //Get specification
        FilterSpecification<Student> filter = new FilterSpecification<Student>();
        Specification<Student> studentSpecification = filter.GetSearchSpecification(pageConfig.getFilters());

        //Get sort
        Sort sort = pageConfig.getSortAndNewItem();

        List<Student> students;
        Page<Student> studentPage = null;
        if (pageConfig.getPageSize() == -1) {
            // Fetch all elements when pageSize is -1
            pageConfig.setCurrentPage(1);
            students = studentRepository.findAll(studentSpecification, sort);
        } else {
            // Fetch paginated data
            Pageable pageable = PageRequest.of(pageConfig.getCurrentPage() - 1, pageConfig.getPageSize(), sort);
            studentPage = studentRepository.findAll(studentSpecification, pageable);
            students = studentPage.getContent();
        }

        // transform data to response DTO
        List<StudentResponse> studentResponses = students.stream().map(studentMapper::toStudentResponse).toList();

        // Set data for page
        PageConfig pageConfigResult;
        if (pageConfig.getPageSize() == -1) {
            pageConfigResult = PageConfig.builder()
                    .pageSize(students.size())
                    .totalRecords(students.size())
                    .totalPage(1)
                    .currentPage(1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();
        } else {
            if (studentPage == null) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);

            pageConfigResult = PageConfig
                    .builder()
                    .pageSize(studentPage.getSize())
                    .totalRecords((int) studentPage.getTotalElements())
                    .totalPage(studentPage.getTotalPages())
                    .currentPage(studentPage.getNumber() + 1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();
        }

        // Build the PagedData object
        result.setResult(PagedData.<StudentResponse, PageConfig>builder()
                .data(studentResponses)
                .pageConfig(pageConfigResult)
                .build());

        return result;
    }

    public ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>> GetRecruitmentPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<RecruitmentResponseShort, PageConfig>>();

        FilterSpecification<Recruitment> filter = new FilterSpecification<>();
        Specification<Recruitment> recruitmentSpecification = filter.GetSearchSpecification(pageConfig.getFilters());

        //Sort
        Sort sort = pageConfig.getSortAndNewItem();

        List<Recruitment> recruitments;
        Page<Recruitment> recruitmentPage = null;
        if (pageConfig.getPageSize() == -1) {
            // Fetch all elements when pageSize is -1
            pageConfig.setCurrentPage(1);
            recruitments = recruitmentRepository.findAll(recruitmentSpecification, sort);
        } else {
            // Fetch paginated data
            Pageable pageable = PageRequest.of(pageConfig.getCurrentPage() - 1, pageConfig.getPageSize(), sort);
            recruitmentPage = recruitmentRepository.findAll(recruitmentSpecification, pageable);
            recruitments = recruitmentPage.getContent();
        }

        // transform data to response DTO
        List<RecruitmentResponseShort> recruitmentResponseShorts = recruitments.stream().map(recruitment -> {
            RecruitmentResponseShort recruitmentResponseShort = recruitmentMapper.toRecruitmentResponseShort(recruitment);
            recruitmentResponseShort.setBusinessName(recruitment.getBusiness().getName());
            recruitmentResponseShort.setBusinessImage("/api/avatar/" + recruitment.getBusiness().getManagedBy().getProfileId());
            return recruitmentResponseShort;
        }).toList();


        // Set data for page
        PageConfig pageConfigResult;
        if (pageConfig.getPageSize() == -1) {
            pageConfigResult = PageConfig.builder()
                    .pageSize(recruitments.size())
                    .totalRecords(recruitments.size())
                    .totalPage(1)
                    .currentPage(1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();

        } else {
            if (recruitmentPage == null) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);

            pageConfigResult = PageConfig
                    .builder()
                    .pageSize(recruitmentPage.getSize())
                    .totalRecords((int) recruitmentPage.getTotalElements())
                    .totalPage(recruitmentPage.getTotalPages())
                    .currentPage(recruitmentPage.getNumber() + 1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();
        }

        // Build the PagedData object
        result.setResult(
                PagedData.<RecruitmentResponseShort, PageConfig>builder()
                        .data(recruitmentResponseShorts)
                        .pageConfig(pageConfigResult)
                        .build()
        );

        result.setCode(200);

        return result;
    }

    public ReturnResult<PagedData<RecruitmentRequestResponse, PageConfig>> GetRecruitmentRequestPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<RecruitmentRequestResponse, PageConfig>>();

        //Specification
        FilterSpecification<RecruitmentRequest> filter = new FilterSpecification<>();
        Specification<RecruitmentRequest> recruitmentRequestSpecification = filter.GetSearchSpecification(pageConfig.getFilters());

        //Sort
        Sort sort = pageConfig.getSortAndNewItem();

        List<RecruitmentRequest> recruitmentRequests;
        Page<RecruitmentRequest> recruitmentRequestPage = null;
        if (pageConfig.getPageSize() == -1) {
            // Fetch all elements when pageSize is -1
            pageConfig.setCurrentPage(1);
            recruitmentRequests = recruitmentRequestRepository.findAll(recruitmentRequestSpecification, sort);
        } else {
            // Fetch paginated data
            Pageable pageable = PageRequest.of(pageConfig.getCurrentPage() - 1, pageConfig.getPageSize(), sort);
            recruitmentRequestPage = recruitmentRequestRepository.findAll(recruitmentRequestSpecification, pageable);
            recruitmentRequests = recruitmentRequestPage.getContent();
        }

        // transform data to response DTO
        List<RecruitmentRequestResponse> recruitmentRequestResponses = recruitmentRequests.stream().map(recruitmentRequest -> {
            RecruitmentRequestResponse recruitmentRequestResponse = recruitmentRequestMapper.toRecruitmentRequestResponse(recruitmentRequest);
            StudentResponse studentResponse = studentMapper.toStudentResponse(recruitmentRequest.getStudent());
            RecruitmentResponse recruitmentResponse = recruitmentMapper.toRecruitmentResponse(recruitmentRequest.getRecruitment());

            recruitmentRequestResponse.setStudent(studentResponse);
            recruitmentRequestResponse.setRecruitment(recruitmentResponse);

            return recruitmentRequestResponse;
        }).toList();

        // Set data for page
        PageConfig pageConfigResult;
        if (pageConfig.getPageSize() == -1) {
            pageConfigResult = PageConfig.builder()
                    .pageSize(recruitmentRequests.size())
                    .totalRecords(recruitmentRequests.size())
                    .totalPage(1)
                    .currentPage(1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();

        } else {
            if (recruitmentRequestPage == null) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);

            pageConfigResult = PageConfig
                    .builder()
                    .pageSize(recruitmentRequestPage.getSize())
                    .totalRecords((int) recruitmentRequestPage.getTotalElements())
                    .totalPage(recruitmentRequestPage.getTotalPages())
                    .currentPage(recruitmentRequestPage.getNumber() + 1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();
        }

        // Build the PagedData object
        result.setResult(
                PagedData.<RecruitmentRequestResponse, PageConfig>builder()
                        .data(recruitmentRequestResponses)
                        .pageConfig(pageConfigResult)
                        .build()
        );
        return result;
    }

    public ReturnResult<PagedData<InstructorResponse, PageConfig>> GetInstructorsPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<InstructorResponse, PageConfig>>();

        //Specification
        FilterSpecification<Instructor> filter = new FilterSpecification<>();
        Specification<Instructor> instructorSpecification = filter.GetSearchSpecification(pageConfig.getFilters());

        //Sort
        Sort sort = pageConfig.getSortAndNewItem();

        List<Instructor> instructors;
        Page<Instructor> instructorPage = null;
        if (pageConfig.getPageSize() == -1) {
            // Fetch all elements when pageSize is -1
            pageConfig.setCurrentPage(1);
            instructors = instructorRepository.findAll(instructorSpecification, sort);
        } else {
            // Fetch paginated data
            Pageable pageable = PageRequest.of(pageConfig.getCurrentPage() - 1, pageConfig.getPageSize(), sort);
            instructorPage = instructorRepository.findAll(instructorSpecification, pageable);
            instructors = instructorPage.getContent();
        }

        // transform data to response DTO
        List<InstructorResponse> instructorResponses = instructors.stream().map(instructor -> {
            InstructorResponse instructorResponse = instructorMapper.toInstructorResponse(instructor);
            ProfileResponse profileResponse = profileMapper.toProfileResponse(instructor.getProfile());
            FacultyResponse facultyResponse = facultyMapper.toFacultyResponse(instructor.getFaculty());

            instructorResponse.setProfile(profileResponse);
            instructorResponse.setFaculty(facultyResponse);

            return instructorResponse;
        }).toList();

        // Set data for page
        PageConfig pageConfigResult;
        if (pageConfig.getPageSize() == -1) {
            pageConfigResult = PageConfig.builder()
                    .pageSize(instructors.size())
                    .totalRecords(instructors.size())
                    .totalPage(1)
                    .currentPage(1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();

        } else {
            if (instructorPage == null) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);

            pageConfigResult = PageConfig
                    .builder()
                    .pageSize(instructorPage.getSize())
                    .totalRecords((int) instructorPage.getTotalElements())
                    .totalPage(instructorPage.getTotalPages())
                    .currentPage(instructorPage.getNumber() + 1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();
        }

        // Build the PagedData object
        result.setResult(
                PagedData.<InstructorResponse, PageConfig>builder()
                        .data(instructorResponses)
                        .pageConfig(pageConfigResult)
                        .build()
        );

        return result;
    }

    public ReturnResult<PagedData<BusinessResponse, PageConfig>> GetBusinessPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<BusinessResponse, PageConfig>>();

        //Specification
        FilterSpecification<Business> filter = new FilterSpecification<>();
        Specification<Business> businessSpecification = filter.GetSearchSpecification(pageConfig.getFilters());

        //Sort
        Sort sort = pageConfig.getSortAndNewItem();

        List<Business> businesses;
        Page<Business> businessPage = null;
        if (pageConfig.getPageSize() == -1) {
            // Fetch all elements when pageSize is -1
            pageConfig.setCurrentPage(1);
            businesses = businessRepository.findAll(businessSpecification, sort);
        } else {
            // Fetch paginated data
            Pageable pageable = PageRequest.of(pageConfig.getCurrentPage() - 1, pageConfig.getPageSize(), sort);
            businessPage = businessRepository.findAll(businessSpecification, pageable);
            businesses = businessPage.getContent();
        }

        // transform data to response DTO
        List<BusinessResponse> businessResponses = businesses.stream().map(businessMapper::toBusinessResponse).toList();

        // Set data for page
        PageConfig pageConfigResult;
        if (pageConfig.getPageSize() == -1) {
            pageConfigResult = PageConfig.builder()
                    .pageSize(businesses.size())
                    .totalRecords(businesses.size())
                    .totalPage(1)
                    .currentPage(1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();

        } else {
            if (businessPage == null) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);

            pageConfigResult = PageConfig
                    .builder()
                    .pageSize(businessPage.getSize())
                    .totalRecords((int) businessPage.getTotalElements())
                    .totalPage(businessPage.getTotalPages())
                    .currentPage(businessPage.getNumber() + 1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();
        }

        // Build the PagedData object
        result.setResult(
                PagedData.<BusinessResponse, PageConfig>builder()
                        .data(businessResponses)
                        .pageConfig(pageConfigResult)
                        .build()
        );

        return result;
    }

    public ReturnResult<PagedData<FacultyResponse, PageConfig>> GetFacultyPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<FacultyResponse, PageConfig>>();

        //Specification
        FilterSpecification<Faculty> filter = new FilterSpecification<>();
        Specification<Faculty> facultySpecification = filter.GetSearchSpecification(pageConfig.getFilters());

        //Sort
        Sort sort = pageConfig.getSortAndNewItem();

        List<Faculty> faculties;
        Page<Faculty> facultyPage = null;
        if (pageConfig.getPageSize() == -1) {
            // Fetch all elements when pageSize is -1
            pageConfig.setCurrentPage(1);
            faculties = facultyRepository.findAll(facultySpecification, sort);
        } else {
            // Fetch paginated data
            Pageable pageable = PageRequest.of(pageConfig.getCurrentPage() - 1, pageConfig.getPageSize(), sort);
            facultyPage = facultyRepository.findAll(facultySpecification, pageable);
            faculties = facultyPage.getContent();
        }

        // transform data to response DTO
        List<FacultyResponse> facultyResponses = faculties.stream().map(facultyMapper::toFacultyResponse).toList();

        // Set data for page
        PageConfig pageConfigResult;
        if (pageConfig.getPageSize() == -1) {
            pageConfigResult = PageConfig.builder()
                    .pageSize(faculties.size())
                    .totalRecords(faculties.size())
                    .totalPage(1)
                    .currentPage(1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();

        } else {
            if (facultyPage == null) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);

            pageConfigResult = PageConfig
                    .builder()
                    .pageSize(facultyPage.getSize())
                    .totalRecords((int) facultyPage.getTotalElements())
                    .totalPage(facultyPage.getTotalPages())
                    .currentPage(facultyPage.getNumber() + 1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();
        }

        // Build the PagedData object
        result.setResult(
                PagedData.<FacultyResponse, PageConfig>builder()
                        .data(facultyResponses)
                        .pageConfig(pageConfigResult)
                        .build()
        );

        return result;
    }

    @Override
    public ReturnResult<PagedData<NotificationResponse, PageConfig>> GetNotificationPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<NotificationResponse, PageConfig>>();

        // Specification
        FilterSpecification<Notification> filter = new FilterSpecification<>();
        Specification<Notification> notificationSpecification = filter.GetSearchSpecification(pageConfig.getFilters());

        // Sort
        Sort sort = pageConfig.getSortAndNewItem();

        List<Notification> notifications;
        Page<Notification> notificationPage = null;
        if (pageConfig.getPageSize() == -1) {
            // Fetch all elements when pageSize is -1
            pageConfig.setCurrentPage(1);
            notifications = notificationRepository.findAll(notificationSpecification, sort);
        } else {
            // Fetch paginated data
            Pageable pageable = PageRequest.of(pageConfig.getCurrentPage() - 1, pageConfig.getPageSize(), sort);
            notificationPage = notificationRepository.findAll(notificationSpecification, pageable);
            notifications = notificationPage.getContent();
        }

        // Transform data to response DTO
        List<NotificationResponse> notificationResponses = notifications.stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();

        // Set data for page
        PageConfig pageConfigResult;
        if (pageConfig.getPageSize() == -1) {
            pageConfigResult = PageConfig.builder()
                    .pageSize(notifications.size())
                    .totalRecords(notifications.size())
                    .totalPage(1)
                    .currentPage(1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();

        } else {
            if (notificationPage == null) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);

            pageConfigResult = PageConfig.builder()
                    .pageSize(notificationPage.getSize())
                    .totalRecords((int) notificationPage.getTotalElements())
                    .totalPage(notificationPage.getTotalPages())
                    .currentPage(notificationPage.getNumber() + 1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();
        }

        // Build the PagedData object
        result.setResult(
                PagedData.<NotificationResponse, PageConfig>builder()
                        .data(notificationResponses)
                        .pageConfig(pageConfigResult)
                        .build()
        );

        return result;
    }

    public ReturnResult<PagedData<BusinessWithRecruitmentsResponse, PageConfig>> GetBusinessWithRecruitmentsPaging(PageConfig pageConfig) {
        var result = new ReturnResult<PagedData<BusinessWithRecruitmentsResponse, PageConfig>>();

        //Specification
        FilterSpecification<Business> filter = new FilterSpecification<>();
        Specification<Business> businessSpecification = filter.GetSearchSpecification(pageConfig.getFilters());

        //Sort
        Sort sort = pageConfig.getSortAndNewItem();

        List<Business> businesses;
        Page<Business> businessPage = null;
        if (pageConfig.getPageSize() == -1) {
            // Fetch all elements when pageSize is -1
            pageConfig.setCurrentPage(1);
            businesses = businessRepository.findAll(businessSpecification, sort);
        } else {
            // Fetch paginated data
            Pageable pageable = PageRequest.of(pageConfig.getCurrentPage() - 1, pageConfig.getPageSize(), sort);
            businessPage = businessRepository.findAll(businessSpecification, pageable);
            businesses = businessPage.getContent();
        }

        // transform data to response DTO
        List<BusinessWithRecruitmentsResponse> businessResponses = businesses.stream()
                .map(business -> {
                    BusinessWithRecruitmentsResponse businessWithRecruitmentsResponse = businessMapper.toBusinessResponseWithRecruitments(business);

                    List<RecruitmentResponse> recruitmentResponses = business.getRecruitments().stream()
                            .filter(recruitment -> recruitment.getStatus() != RecruitmentStatus.CLOSED) // filter out closed recruitments
                            .map(recruitment -> {
                                RecruitmentResponse recruitmentResponse = recruitmentMapper.toRecruitmentResponse(recruitment);
                                recruitmentResponse.setBusiness(null); // set Business to null as needed
                                return recruitmentResponse;
                            })
                            .toList();


                    businessWithRecruitmentsResponse.setRecruitments(recruitmentResponses);

                    return businessWithRecruitmentsResponse;
                })
                .toList();

        // Set data for page
        PageConfig pageConfigResult;
        if (pageConfig.getPageSize() == -1) {
            pageConfigResult = PageConfig.builder()
                    .pageSize(businesses.size())
                    .totalRecords(businesses.size())
                    .totalPage(1)
                    .currentPage(1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();

        } else {
            if (businessPage == null) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);

            pageConfigResult = PageConfig
                    .builder()
                    .pageSize(businessPage.getSize())
                    .totalRecords((int) businessPage.getTotalElements())
                    .totalPage(businessPage.getTotalPages())
                    .currentPage(businessPage.getNumber() + 1)
                    .orders(pageConfig.getOrders())
                    .filters(pageConfig.getFilters())
                    .build();
        }

        // Build the PagedData object
        result.setResult(
                PagedData.<BusinessWithRecruitmentsResponse, PageConfig>builder()
                        .data(businessResponses)
                        .pageConfig(pageConfigResult)
                        .build()
        );

        return result;
    }
}

