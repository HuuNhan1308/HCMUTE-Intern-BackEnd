package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.ProfileMapper;
import com.intern.app.models.dto.request.InstructorCreationRequest;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Faculty;
import com.intern.app.models.entity.Instructor;
import com.intern.app.models.entity.Profile;
import com.intern.app.models.entity.Role;
import com.intern.app.repository.FacultyRepository;
import com.intern.app.repository.InstructorRepository;
import com.intern.app.repository.RoleRepository;
import com.intern.app.services.interfaces.IInstructorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class InstructorService implements IInstructorService {
    ProfileService profileService;
    RoleRepository roleRepository;
    FacultyRepository facultyRepository;
    InstructorRepository instructorRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public ReturnResult<Boolean> CreateInstructor(InstructorCreationRequest instructorCreationRequest) {
        var result = new ReturnResult<Boolean>();

        Role instructorRole = roleRepository.findByRoleName("INSTRUCTOR")
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));

        Profile createdProfile = profileService.CreateUser(instructorCreationRequest.getProfile(), instructorRole).getResult();

        Faculty faculty = facultyRepository.findByFacultyId(instructorCreationRequest.getFacultyId())
                .orElseThrow(() -> new AppException(ErrorCode.FACULTY_NOT_EXISTED));

        Instructor instructor = Instructor.builder()
                .faculty(faculty)
                .profile(createdProfile)
                .build();

        instructorRepository.save(instructor);

        result.setResult(Boolean.TRUE);
        result.setCode(200);

        return result;
    }
}
