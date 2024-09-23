package com.intern.app.configuration;


import com.github.javafaker.Faker;
import com.intern.app.models.entity.*;
import com.intern.app.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataInitialize {

    RoleRepository roleRepository;
    ProfileRepository profileRepository;
    FacultyRepository facultyRepository;
    StudentRepository studentRepository;
    MajorRepository majorRepository;
    PasswordEncoder passwordEncoder;
    Faker faker = new Faker();

//    @PostConstruct
    public void initializeRoles() {

        // ROLE
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("STUDENT");


        //FACULTY
        createFacultyIfNotExist("FIE");
        createFacultyIfNotExist("AAA");
        createFacultyIfNotExist("AOA");
        createFacultyIfNotExist("HRM");


        //MAJOR
        createMajorIfNotExist("Công nghệ thông tin");
        createMajorIfNotExist("Công nghệ thông tin " + faker.number().randomNumber(3, true));
        createMajorIfNotExist("Công nghệ thông tin " + faker.number().randomNumber(3, true));
        createMajorIfNotExist("Công nghệ thông tin " + faker.number().randomNumber(3, true));
        createMajorIfNotExist("Công nghệ thông tin " + faker.number().randomNumber(3, true));

        //STUDENT
        createStudentIfNotExists("21110787", createProfileIfNotExists("nhan", "nhan", "STUDENT").getProfileId());
        for (int i = 1; i < 10; ++i) {
            String iAsString = String.valueOf(i);
            Profile newStudentProfile = createProfileIfNotExists("student" + iAsString, iAsString, "STUDENT");
            createStudentIfNotExists(null, newStudentProfile.getProfileId());
        }

        // ADMIN
        createProfileIfNotExists("admin", "1", "ADMIN");
    }

    private void createRoleIfNotExists(String roleName) {
        if (!roleRepository.findByRoleName(roleName).isPresent()) {
            Role role = Role.builder()
                    .roleName(roleName)
                    .build();
            roleRepository.save(role);
        }
    }

    private Profile createProfileIfNotExists(String userName, String password, String roleName) {
        Role role = roleRepository.findByRoleName(roleName).orElse(null);
        if(!profileRepository.findByUsernameAndPassword(userName, password).isPresent()) {

            Profile profile = Profile.builder()
                    .username(userName)
                    .password(this.passwordEncoder.encode(password))
                    .role(role)
                    .fullname(faker.name().fullName())
                    .bio(faker.lorem().paragraph(2))
                    .email(faker.internet().emailAddress())
                    .isMale(faker.bool().bool())
                    .phoneNumber(faker.phoneNumber().phoneNumber())
                    .build();

            return profileRepository.save(profile);
        }

        return profileRepository.findByUsername(userName).get();
    }

    private void createFacultyIfNotExist(String name) {
        if(!facultyRepository.findByName(name).isPresent()) {
            Faculty faculty = Faculty.builder()
                    .name(name)
                    .build();
            facultyRepository.save(faculty);
        }
    }

    private Student createStudentIfNotExists(String Id, String profileId) {
        Faculty FIE = facultyRepository.findByName("FIE").orElse(null);
        Profile profile = profileRepository.findById(profileId).orElse(null);
        Major major = majorRepository.findByName("Công nghệ thông tin").orElse(null);


        while(Id == null || studentRepository.findById(Id).isPresent()) {
            Id = String.valueOf(faker.number().randomNumber(8, true));
        }

        if(!studentRepository.findById(Id).isPresent()) {
            Student student = Student.builder()
                    .studentId(Id)
                    .faculty(FIE)
                    .year("K23")
                    .isSeekingIntern(false)
                    .dob(faker.date().birthday())
                    .profile(profile)
                    .major(major)
                    .build();

            return studentRepository.save(student);
        }
        return null;
    }

    private Major createMajorIfNotExist(String name) {
        Optional<Major> major = majorRepository.findByName(name);
        if(!major.isPresent()) {
            String Id;
            do
                Id = String.valueOf(faker.number().randomNumber(8, true));
            while(studentRepository.findById(Id).isPresent());

            Major createdMajor = Major.builder()
                    .majorId(Id)
                    .name(name)
                    .build();

            return majorRepository.save(createdMajor);
        }
        else {
            return major.get();
        }
    }
}