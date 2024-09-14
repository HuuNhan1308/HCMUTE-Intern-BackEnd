package com.intern.app.configuration;


import com.github.javafaker.Faker;
import com.intern.app.models.entity.Faculty;
import com.intern.app.models.entity.Profile;
import com.intern.app.models.entity.Role;
import com.intern.app.models.entity.Student;
import com.intern.app.repository.FacultyRepository;
import com.intern.app.repository.ProfileRepository;
import com.intern.app.repository.RoleRepository;
import com.intern.app.repository.StudentRepository;
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

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataInitialize {

    RoleRepository roleRepository;
    ProfileRepository profileRepository;
    FacultyRepository facultyRepository;
    StudentRepository studentRepository;
    PasswordEncoder passwordEncoder;
    Faker faker = new Faker();

    @PostConstruct
    public void initializeRoles() {
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("STUDENT");

        createFacultyIfNotExist("FIE");
        createFacultyIfNotExist("AAA");
        createFacultyIfNotExist("AOA");
        createFacultyIfNotExist("HRM");


        createStudentIfNotExists(21110787L, createProfileIfNotExists("nhan", "nhan", "STUDENT").getProfileId());
        for (int i = 1; i < 10; ++i) {
            String iAsString = String.valueOf(i);
            Profile newStudentProfile = createProfileIfNotExists("student" + iAsString, iAsString, "STUDENT");
            createStudentIfNotExists(null, newStudentProfile.getProfileId());
        }

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
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .bio(faker.lorem().paragraph(2))
                    .email(faker.internet().emailAddress())
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

    private Student createStudentIfNotExists(Long Id, String profileId) {
        Faculty FIE = facultyRepository.findByName("FIE").orElse(null);
        Profile profile = profileRepository.findById(profileId).orElse(null);

        while(Id == null || studentRepository.findById(Id).isPresent()) {
            Id = faker.number().randomNumber(8, true);
        }

        if(!studentRepository.findById(Id).isPresent()) {
            Student student = Student.builder()
                    .studentId(Id)
                    .faculty(FIE)
                    .year("K23")
                    .isSeekingIntern(false)
                    .dob(faker.date().birthday())
                    .profile(profile)
                    .build();

            return studentRepository.save(student);
        }
        return null;
    }
}