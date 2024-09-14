package com.intern.app.services;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.mapper.FacultyMapper;
import com.intern.app.mapper.ProfileMapper;
import com.intern.app.mapper.StudentMapper;
import com.intern.app.models.dto.response.FacultyResponse;
import com.intern.app.models.dto.response.ProfileResponse;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.dto.response.StudentResponse;
import com.intern.app.models.entity.Profile;
import com.intern.app.models.entity.Student;
import com.intern.app.repository.ProfileRepository;
import com.intern.app.repository.StudentRepository;
import com.nimbusds.jose.Payload;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentService {
    StudentRepository studentRepository;
    AuthenticationService authenticationService;
    StudentMapper studentMapper;
    FacultyMapper facultyMapper;
    ProfileMapper profileMapper;
    private final ProfileRepository profileRepository;

    public ReturnResult<StudentResponse> FindStudentById(Long id) {
        var result = new ReturnResult<StudentResponse>();
        Student student = studentRepository.findById(id).orElse(null);

        if(student == null) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        } else {
            StudentResponse studentResponse = studentMapper.toStudentResponse(student);
            studentResponse.setFaculty(facultyMapper.toFacultyResponse(student.getFaculty()));
            studentResponse.setProfile(profileMapper.toProfileResponse(student.getProfile()));

            result.setResult(studentResponse);
            result.setCode(HttpStatus.OK.value());
        }

        return result;
    }

    public ReturnResult<StudentResponse>FindStudentByAccessToken(String token) throws ParseException {
        var result = new ReturnResult<StudentResponse>();

        var data = authenticationService.verityToken(token).getJWTClaimsSet();
        String username = data.getSubject();

        Profile profile = profileRepository.findByUsername(username).orElse(null);
        if(profile.getDeleted()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        Student student = studentRepository.findByProfile(profile).orElse(null);
        if(student == null) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        } else {
            StudentResponse studentResponse = studentMapper.toStudentResponse(student);
            studentResponse.setFaculty(facultyMapper.toFacultyResponse(student.getFaculty()));
            studentResponse.setProfile(profileMapper.toProfileResponse(student.getProfile()));

            result.setResult(studentResponse);
            result.setCode(HttpStatus.OK.value());
        }
        return result;
    }
}
