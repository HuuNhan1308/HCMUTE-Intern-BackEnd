package com.intern.app.configuration;

import com.github.javafaker.Faker;
import com.intern.app.models.entity.*;
import com.intern.app.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
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
import java.util.Random;

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

    // @PostConstruct
    public void initializeRoles() {

        // ROLE
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("MANAGER");
        createRoleIfNotExists("BUSINESS");
        createRoleIfNotExists("INSTRUCTOR");
        createRoleIfNotExists("STUDENT");

        // FACULTY
        createFacultyIfNotExist("KHOA CHÍNH TRỊ LUẬT", "FPI");
        createFacultyIfNotExist("KHOA CƠ KHÍ CHẾ TẠO MÁY", "FME");
        createFacultyIfNotExist("KHOA CƠ KHÍ ĐỘNG LỰC", "FAE");
        createFacultyIfNotExist("KHOA CÔNG NGHỆ HÓA HỌC VÀ THỰC PHẨM", "FCFT");
        createFacultyIfNotExist("KHOA CÔNG NGHỆ THÔNG TIN", "FIT");
        createFacultyIfNotExist("KHOA ĐIỆN - ĐIỆN TỬ", "FEEE");
        createFacultyIfNotExist("KHOA IN VÀ TRUYỀN THÔNG", "FGAM");
        createFacultyIfNotExist("KHOA KHOA HỌC ỨNG DỤNG", "FAS");
        createFacultyIfNotExist("KHOA KINH TẾ", "FE");
        createFacultyIfNotExist("KHOA NGOẠI NGỮ", "FFL");
        createFacultyIfNotExist("KHOA THỜI TRANG VÀ DU LỊCH", "FGTFD");
        createFacultyIfNotExist("KHOA XÂY DỰNG", "FCE");
        createFacultyIfNotExist("VIỆN SƯ PHẠM KỸ THUẬT", "ITE");
        createFacultyIfNotExist("KHOA ĐÀO TẠO CHẤT LƯỢNG CAO", "FHQ");
        createFacultyIfNotExist("KHOA ĐÀO TẠO QUỐC TẾ", "FIE");

        // MAJOR
        createMajorIfNotExist("Ngành Luật", "FPI");

        createMajorIfNotExist("Ngành Kỹ thuật Công nghiệp", "FME");
        createMajorIfNotExist("Ngành Robot và trí tuệ nhân tạo", "FME");
        createMajorIfNotExist("Ngành Kỹ nghệ gỗ và nội thất", "FME");
        createMajorIfNotExist("Ngành Công nghệ Chế tạo máy", "FME");
        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Cơ khí", "FME");
        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Cơ điện tử", "FME");

        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Ô tô", "FAE");
        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Nhiệt", "FAE");
        createMajorIfNotExist("Ngành Năng lượng tái tạo", "FAE");

        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Hóa học", "FCFT");
        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Môi trường", "FCFT");
        createMajorIfNotExist("Ngành Công nghệ Thực phẩm", "FCFT");

        createMajorIfNotExist("Ngành An toàn thông tin", "FIT");
        createMajorIfNotExist("Ngành Công nghệ Thông tin", "FIT");
        createMajorIfNotExist("Ngành Kỹ thuật dữ liệu", "FIT");

        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Điện Điện tử", "FEEE");
        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Điện tử - Viễn thông", "FEEE");
        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Điều khiển và Tự động hóa", "FEEE");
        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Máy tính", "FEEE");
        createMajorIfNotExist("Ngành Hệ thống nhúng và IOT", "FEEE");
        createMajorIfNotExist("Ngành Kỹ thuật Y sinh", "FEEE");

        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật In", "FGAM");
        createMajorIfNotExist("Ngành Thiết kế đồ họa", "FGAM");

        createMajorIfNotExist("Ngành Công nghệ Vật liệu", "FAS");

        createMajorIfNotExist("Ngành Kế toán", "FE");
        createMajorIfNotExist("Ngành Kinh doanh Quốc tế", "FE");
        createMajorIfNotExist("Ngành Logistics và Quản lý chuỗi cung ứng", "FE");
        createMajorIfNotExist("Ngành Quản lý Công nghiệp", "FE");
        createMajorIfNotExist("Ngành Thương mại Điện tử", "FE");

        createMajorIfNotExist("Ngành Sư phạm tiếng Anh", "FFL");
        createMajorIfNotExist("Ngành Biên phiên dịch tiếng Anh Kỹ thuật", "FFL");
        createMajorIfNotExist("Ngành Tiếng Anh thương mại", "FFL");

        createMajorIfNotExist("Ngành Công nghệ May", "FGTFD");
        createMajorIfNotExist("Ngành Quản trị nhà hàng và dịch vụ ăn uống", "FGTFD");
        createMajorIfNotExist("Ngành Thiết kế thời trang", "FGTFD");

        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Công trình xây dựng", "FCE");
        createMajorIfNotExist("Ngành Hệ thống Kỹ thuật Công trình xây dựng", "FCE");
        createMajorIfNotExist("Ngành Kỹ thuật xây dựng Công trình giao thông", "FCE");
        createMajorIfNotExist("Ngành Quản lý và vận hành hạ tầng", "FCE");
        createMajorIfNotExist("Ngành Quản lý xây dựng", "FCE");
        createMajorIfNotExist("Ngành Kiến trúc", "FCE");
        createMajorIfNotExist("Ngành Kiến trúc nội thất", "FCE");

        createMajorIfNotExist("Ngành Sư phạm Công nghệ", "ITE");

        createMajorIfNotExist("Ngành Công nghệ May", "FHQ");
        createMajorIfNotExist("Ngành Thiết kế thời trang", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ Thông tin", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ Thực phẩm", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Máy tính", "FHQ");
        createMajorIfNotExist("Ngành Quản lý Công nghiệp", "FHQ");
        createMajorIfNotExist("Ngành Kế toán", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Điện tử Viễn thông", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Điện tử Viễn thông Việt – Nhật", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Điện Điện tử", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ Chế tạo máy", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ Chế tạo máy Việt – Nhật", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ kỹ thuật Cơ khí", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ kỹ thuật Ô tô", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Cơ điện tử", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Nhiệt", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật In", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ Kỹ Thuật Công trình Xây dựng", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ kỹ thuật Môi trường", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ Điều khiển và Tự động hóa", "FHQ");
        createMajorIfNotExist("Ngành Công nghệ Kỹ Thuật Hóa Học", "FHQ");
        createMajorIfNotExist("Ngành Thương mại Điện tử", "FHQ");

        createMajorIfNotExist("Ngành Công nghệ kỹ thuật điện, điện tử", "FIE");
        createMajorIfNotExist("Ngành Công nghệ kỹ thuật điện tử, viễn thông", "FIE");
        createMajorIfNotExist("Ngành Công nghệ kỹ thuật điều khiển và tự động hóa", "FIE");
        createMajorIfNotExist("Ngành Công nghệ kỹ thuật cơ khí", "FIE");
        createMajorIfNotExist("Ngành Công nghệ chế tạo máy", "FIE");
        createMajorIfNotExist("Ngành Công nghệ kỹ thuật cơ điện tử", "FIE");
        createMajorIfNotExist("Ngành Công nghệ kỹ thuật ô tô", "FIE");
        createMajorIfNotExist("Ngành Công nghệ kỹ thuật công trình xây dựng", "FIE");
        createMajorIfNotExist("Ngành Công nghệ thông tin", "FIE");
        createMajorIfNotExist("Ngành Công nghệ thực phẩm", "FIE");
        createMajorIfNotExist("Ngành Công nghệ kỹ thuật máy tính", "FIE");
        createMajorIfNotExist("Ngành Công nghệ Kỹ thuật Nhiệt", "FIE");
        createMajorIfNotExist("Ngành Quản lý công nghiệp", "FIE");

        // STUDENT
        createStudentIfNotExists("21110787", createProfileIfNotExists("21110787", "21110787",
                "STUDENT").getProfileId());

        for (int i = 21110000; i <= 21110100; i++) {
            String iAsString = String.valueOf(i);
            createStudentIfNotExists(iAsString, createProfileIfNotExists(iAsString, iAsString,
                    "STUDENT").getProfileId());
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
        if (!profileRepository.findByUsernameAndPassword(userName, password).isPresent()) {

            Profile profile = Profile.builder()
                    .username(userName)
                    .password(this.passwordEncoder.encode(password))
                    .role(role)
                    .fullname(faker.name().fullName())
                    .bio(faker.lorem().paragraph(4))
                    .email(faker.internet().emailAddress())
                    .isMale(faker.bool().bool())
                    .phoneNumber(faker.phoneNumber().phoneNumber())
                    .build();

            return profileRepository.save(profile);
        }

        return profileRepository.findByUsername(userName).get();
    }

    private void createFacultyIfNotExist(String facultyName, String facultyId) {
        if (!facultyRepository.findByName(facultyName).isPresent()) {
            Faculty faculty = Faculty.builder()
                    .name(facultyName)
                    .facultyId(facultyId)
                    .build();
            facultyRepository.save(faculty);
        }
    }

    private Student createStudentIfNotExists(String Id, String profileId) {
        Profile profile = profileRepository.findById(profileId).orElse(null);

        List<Major> allMajor = majorRepository.findAll();

        while (Id == null || studentRepository.findById(Id).isPresent()) {
            Id = String.valueOf(faker.number().randomNumber(8, true));
        }

        if (!studentRepository.findById(Id).isPresent()) {
            Major randomMajor = allMajor.get(new Random().nextInt(allMajor.size()));
            Student student = Student.builder()
                    .studentId(Id)
                    .year(new Random().nextInt(4) + 2021)
                    .isSeekingIntern(false)
                    .dob(faker.date().birthday())
                    .profile(profile)
                    .major(randomMajor)
                    .build();

            return studentRepository.save(student);
        }
        return null;
    }

    private void createMajorIfNotExist(String majorName, String facultyId) {
        Optional<Faculty> facultyOptional = facultyRepository.findById(facultyId);

        Faculty faculty = facultyOptional.orElseThrow(() -> new RuntimeException("Faculty not found"));

        Major newMajor = Major.builder()
                .name(majorName)
                .faculty(faculty)
                .build();

        majorRepository.save(newMajor);
    }
}