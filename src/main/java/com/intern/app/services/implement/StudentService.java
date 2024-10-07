package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.FacultyMapper;
import com.intern.app.mapper.MajorMapper;
import com.intern.app.mapper.ProfileMapper;
import com.intern.app.mapper.StudentMapper;
import com.intern.app.mapper.UploadContentMapper;
import com.intern.app.models.dto.datamodel.PagedData;
import com.intern.app.models.dto.datamodel.StudentPageConfig;
import com.intern.app.models.dto.request.StudentCreationRequest;
import com.intern.app.models.dto.request.StudentUpdateRequest;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.dto.response.StudentResponse;
import com.intern.app.models.entity.Profile;
import com.intern.app.models.entity.Student;
import com.intern.app.repository.FacultyRepository;
import com.intern.app.repository.MajorRepository;
import com.intern.app.repository.ProfileRepository;
import com.intern.app.repository.StudentRepository;

import com.intern.app.services.interfaces.IStudentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentService implements IStudentService {
    StudentRepository studentRepository;
    AuthenticationService authenticationService;
    StudentMapper studentMapper;
    FacultyMapper facultyMapper;
    ProfileMapper profileMapper;
    MajorMapper majorMapper;
    UploadContentMapper uploadContentMapper;
    ProfileRepository profileRepository;
    MajorRepository majorRepository;
    FacultyRepository facultyRepository;

    public ReturnResult<StudentResponse> FindStudentById(String id) {
        var result = new ReturnResult<StudentResponse>();
        Student student = studentRepository.findById(id).orElse(null);

        if (student == null) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        } else {
            StudentResponse studentResponse = studentMapper.toStudentResponse(student);
            studentResponse.setMajor(majorMapper.toMajorResponse(student.getMajor()));
            studentResponse.setProfile(profileMapper.toProfileResponse(student.getProfile()));

            result.setResult(studentResponse);
            result.setCode(HttpStatus.OK.value());
        }

        return result;
    }

    public ReturnResult<StudentResponse> GetStudentByUsername(String username) {
        var result = new ReturnResult<StudentResponse>();

        Profile profile = profileRepository.findByUsername(username).orElse(null);
        if (profile.getDeleted()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        Student student = studentRepository.findByProfile(profile).orElse(null);
        if (student == null) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        } else {
            StudentResponse studentResponse = studentMapper.toStudentResponse(student);
            studentResponse.setMajor(majorMapper.toMajorResponse(student.getMajor()));
            studentResponse.setProfile(profileMapper.toProfileResponse(student.getProfile()));

            result.setResult(studentResponse);
            result.setCode(HttpStatus.OK.value());
        }

        return result;
    }

    // NOT FINISH
    public ReturnResult<Boolean> CreateStudent(StudentCreationRequest studentCreationRequest) {
        ReturnResult<Boolean> result = new ReturnResult<>();

        Boolean isStudentExist = studentRepository.existsById(studentCreationRequest.getStudentId());

        if (isStudentExist) {
            throw new AppException(ErrorCode.STUDENT_EXISTED_ID);
        }

        Student student = studentMapper.toStudent(studentCreationRequest);

        return result;
    }

    public ReturnResult<PagedData<StudentResponse, StudentPageConfig>> GetAllStudentPaging(StudentPageConfig page) {
        var result = new ReturnResult<PagedData<StudentResponse, StudentPageConfig>>();

        Sort sort = page.getSort();
        Pageable pageable = PageRequest.of(page.getCurrentPage() - 1, page.getPageSize(), sort);

        // Fetch the filtered and paginated data from the repository
        Page<Student> studentPage;
        if (!page.getMajorIds().isEmpty()) {
            studentPage = studentRepository.findByFullnameContainingIgnoreCaseAndMajorIdIn(page.getFullname(),
                    page.getMajorIds(), pageable);
        } else {
            studentPage = studentRepository.findByFullnameContainingIgnoreCase(page.getFullname(), pageable);
        }

        // Convert Student entities to StudentResponse DTOs
        List<StudentResponse> studentResponses = studentPage.getContent().stream()
                .map(student -> {
                    StudentResponse studentResponse = studentMapper.toStudentResponse(student);
                    studentResponse.setProfile(profileMapper.toProfileResponse(student.getProfile()));
                    return studentResponse;
                })
                .toList();

        // Set data for page
        StudentPageConfig newPageConfig = StudentPageConfig.builder()
                .currentPage(studentPage.getNumber() + 1)
                .pageSize(studentPage.getSize())
                .totalRecords((int) studentPage.getTotalElements())
                .totalPage(studentPage.getTotalPages())
                .orders(page.getOrders())
                .fullname(page.getFullname())
                .majorIds(page.getMajorIds())
                .build();

        // Build the PagedData object
        result.setResult(PagedData.<StudentResponse, StudentPageConfig>builder()
                .data(studentResponses)
                .pageConfig(newPageConfig)
                .build());

        return result;
    }

    public ReturnResult<Boolean> UpdateStudent(StudentUpdateRequest studentUpdateRequest) {
        var result = new ReturnResult<Boolean>();

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        Profile profile = profileRepository.findByUsername(username).orElse(null);
        Student student = profile.getStudent();

        if (profile == null || student == null) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }

        studentMapper.updateStudent(student, studentUpdateRequest);
        profileMapper.updateProfile(profile, studentUpdateRequest.getProfile());

        studentRepository.save(student);
        profileRepository.save(profile);

        return result;
    }
}
