package com.intern.app.configuration;

import com.github.javafaker.Faker;
import com.intern.app.mapper.ProfileMapper;
import com.intern.app.models.dto.request.ProfileCreationRequest;
import com.intern.app.models.entity.*;
import com.intern.app.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataInitialize {

    RoleRepository roleRepository;
    ProfileRepository profileRepository;
    FacultyRepository facultyRepository;
    StudentRepository studentRepository;
    MajorRepository majorRepository;
    BusinessRepository businessRepository;

    ProfileMapper profileMapper;

    PasswordEncoder passwordEncoder;
    Faker faker = new Faker();

    @PostConstruct
    public void initializeRoles() {

        // ROLE
        createRoles();

        // FACULTY
        createFaculties();

        // MAJOR
        createMajors();

        // STUDENT
        createStudents();

        // ADMIN
        createProfileIfNotExists("admin", "1", "ADMIN");

        // Business
        createBusinesses();
    }

    private void createStudents() {
        for (int i = 21110000; i <= 21110050; i++) {
            String iAsString = String.valueOf(i);
            List<Major> allMajor = majorRepository.findAll();


            if (studentRepository.findById(iAsString).isEmpty()) {
                Profile profileCreated = createProfileIfNotExists(iAsString, iAsString, "STUDENT");
                Major randomMajor = allMajor.get(new Random().nextInt(allMajor.size()));

                Student student = Student.builder()
                        .studentId(iAsString)
                        .year(new Random().nextInt(4) + 2021)
                        .isSeekingIntern(false)
                        .dob(faker.date().birthday())
                        .profile(profileCreated)
                        .major(randomMajor)
                        .build();

                studentRepository.save(student);
            }
        }
    }

    private Profile createProfileIfNotExists(String userName, String password, String roleName) {
        Role role = roleRepository.findByRoleName(roleName).orElse(null);
        if (profileRepository.findByUsername(userName).isEmpty()) {

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

    private void createBusinesses() {
        List<String> businessNames = Arrays.asList(
                "Công ty Cổ phần Sữa Việt Nam (Vinamilk)",
                "Tập đoàn Vingroup – Công ty CP",
                "Công ty CP Tập đoàn Hòa Phát",
                "Ngân hàng TMCP Ngoại thương Việt Nam (Vietcombank)",
                "Công ty Cổ phần FPT",
                "Tập đoàn Bưu chính Viễn thông Việt Nam (VNPT)",
                "Công ty Cổ phần Đầu tư Thế Giới Di Động",
                "Tập đoàn Masan",
                "Ngân hàng TMCP Công Thương Việt Nam (VietinBank)",
                "Công ty Cổ phần Vàng bạc Đá quý Phú Nhuận (PNJ)");

        for (int i = 0; i < businessNames.size(); i++) {
            // if exist then continue
            if (businessRepository.findByName(businessNames.get(i)).isPresent()) {
                continue;
            } else {
                String index = String.valueOf(i + 1); // Adding 1 to make the index start from 1

                ProfileCreationRequest profileCreation = ProfileCreationRequest
                        .builder()
                        .fullname(faker.name().fullName())
                        .username("business" + index)
                        .password("business" + index)
                        .isMale(faker.bool().bool())
                        .bio(faker.lorem().paragraph(2))
                        .email(faker.internet().emailAddress())
                        .phoneNumber(faker.phoneNumber().phoneNumber())
                        .build();

                Profile profile = profileMapper.toProfile(profileCreation);
                profile.setRole(roleRepository.findByRoleName("BUSINESS").get());

                Profile createdProfile = profileRepository.save(profile);

                Business businessCreation = Business.builder()
                        .name(businessNames.get(i))
                        .type("Business")
                        .overview(faker.lorem().paragraph(2))
                        .workingDay("8")
                        .workingHour("9:00 - 18:30")
                        .industry("IT")
                        .location(faker.address().streetAddressNumber())
                        .managedBy(createdProfile)
                        .build();

                businessRepository.save(businessCreation);
            }
        }
    }

    private void createFaculties() {
        // Khởi tạo Map chứa dữ liệu các khoa
        Map<String, String> facultyMap = new HashMap<>();

        // Thêm dữ liệu vào Map
        facultyMap.put("FPI", "KHOA CHÍNH TRỊ LUẬT");
        facultyMap.put("FME", "KHOA CƠ KHÍ CHẾ TẠO MÁY");
        facultyMap.put("FAE", "KHOA CƠ KHÍ ĐỘNG LỰC");
        facultyMap.put("FCFT", "KHOA CÔNG NGHỆ HÓA HỌC VÀ THỰC PHẨM");
        facultyMap.put("FIT", "KHOA CÔNG NGHỆ THÔNG TIN");
        facultyMap.put("FEEE", "KHOA ĐIỆN - ĐIỆN TỬ");
        facultyMap.put("FGAM", "KHOA IN VÀ TRUYỀN THÔNG");
        facultyMap.put("FAS", "KHOA KHOA HỌC ỨNG DỤNG");
        facultyMap.put("FE", "KHOA KINH TẾ");
        facultyMap.put("FFL", "KHOA NGOẠI NGỮ");
        facultyMap.put("FGTFD", "KHOA THỜI TRANG VÀ DU LỊCH");
        facultyMap.put("FCE", "KHOA XÂY DỰNG");
        facultyMap.put("ITE", "VIỆN SƯ PHẠM KỸ THUẬT");
        facultyMap.put("FHQ", "KHOA ĐÀO TẠO CHẤT LƯỢNG CAO");
        facultyMap.put("FIE", "KHOA ĐÀO TẠO QUỐC TẾ");

        for (Map.Entry<String, String> entry : facultyMap.entrySet()) {

            if (facultyRepository.findByFacultyId(entry.getKey()).isPresent())
                continue;
            else {
                facultyRepository.save(Faculty.builder()
                        .facultyId(entry.getKey())
                        .name(entry.getValue())
                        .build());
            }
        }
    }

    private void createMajors() {


        List<Major> initMajors = List.of(
                Major.builder().faculty(Faculty.builder().facultyId("FPI").build()).name("Ngành Luật").build(),

                Major.builder().faculty(Faculty.builder().facultyId("FME").build()).name("Ngành Kỹ thuật Công nghiệp").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FME").build()).name("Ngành Robot và trí tuệ nhân tạo").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FME").build()).name("Ngành Kỹ nghệ gỗ và nội thất").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FME").build()).name("Ngành Công nghệ Chế tạo máy").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FME").build()).name("Ngành Công nghệ Kỹ thuật Cơ khí").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FME").build()).name("Ngành Công nghệ Kỹ thuật Cơ điện tử").build(),

                Major.builder().faculty(Faculty.builder().facultyId("FAE").build()).name("Ngành Công nghệ Kỹ thuật Ô tô").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FAE").build()).name("Ngành Công nghệ Kỹ thuật Nhiệt").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FAE").build()).name("Ngành Năng lượng tái tạo").build(),

                Major.builder().faculty(Faculty.builder().facultyId("FCFT").build()).name("Ngành Công nghệ Kỹ thuật Hóa học").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FCFT").build()).name("Ngành Công nghệ Kỹ thuật Môi trường").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FCFT").build()).name("Ngành Công nghệ Thực phẩm").build(),

                Major.builder().faculty(Faculty.builder().facultyId("FIT").build()).name("Ngành An toàn thông tin").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FIT").build()).name("Ngành Công nghệ Thông tin").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FIT").build()).name("Ngành Kỹ thuật dữ liệu").build(),

                Major.builder().faculty(Faculty.builder().facultyId("FEEE").build()).name("Ngành Công nghệ Kỹ thuật Điện Điện tử").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FEEE").build()).name("Ngành Công nghệ Kỹ thuật Điện tử - Viễn thông").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FEEE").build()).name("Ngành Công nghệ Kỹ thuật Điều khiển và Tự động hóa").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FEEE").build()).name("Ngành Công nghệ Kỹ thuật Máy tính").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FEEE").build()).name("Ngành Hệ thống nhúng và IOT").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FEEE").build()).name("Ngành Kỹ thuật Y sinh").build(),

                Major.builder().faculty(Faculty.builder().facultyId("FGAM").build()).name("Ngành Công nghệ Kỹ thuật In").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FGAM").build()).name("Ngành Thiết kế đồ họa").build(),

                Major.builder().faculty(Faculty.builder().facultyId("FAS").build()).name("Ngành Công nghệ Vật liệu").build(),

                Major.builder().faculty(Faculty.builder().facultyId("FE").build()).name("Ngành Kế toán").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FE").build()).name("Ngành Kinh doanh Quốc tế").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FE").build()).name("Ngành Logistics và Quản lý chuỗi cung ứng").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FE").build()).name("Ngành Quản lý Công nghiệp").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FE").build()).name("Ngành Thương mại Điện tử").build(),

                Major.builder().faculty(Faculty.builder().facultyId("FFL").build()).name("Ngành Sư phạm tiếng Anh").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FFL").build()).name("Ngành Biên phiên dịch tiếng Anh Kỹ thuật").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FFL").build()).name("Ngành Tiếng Anh thương mại").build(),

                Major.builder().faculty(Faculty.builder().facultyId("FGTFD").build()).name("Ngành Công nghệ May").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FGTFD").build()).name("Ngành Quản trị nhà hàng và dịch vụ ăn uống").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FGTFD").build()).name("Ngành Thiết kế thời trang").build(),

                Major.builder().faculty(Faculty.builder().facultyId("FCE").build()).name("Ngành Công nghệ Kỹ thuật Công trình xây dựng").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FCE").build()).name("Ngành Hệ thống Kỹ thuật Công trình xây dựng").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FCE").build()).name("Ngành Kỹ thuật xây dựng Công trình giao thông").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FCE").build()).name("Ngành Quản lý và vận hành hạ tầng").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FCE").build()).name("Ngành Quản lý xây dựng").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FCE").build()).name("Ngành Kiến trúc").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FCE").build()).name("Ngành Kiến trúc nội thất").build(),

                Major.builder().faculty(Faculty.builder().facultyId("ITE").build()).name("Ngành Sư phạm Công nghệ").build(),

                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ May").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Thiết kế thời trang").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ Thông tin").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ Thực phẩm").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ Kỹ thuật Máy tính").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Quản lý Công nghiệp").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Kế toán").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ Kỹ thuật Điện tử Viễn thông").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ Kỹ thuật Điện tử Viễn thông Việt – Nhật").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ Kỹ thuật Điện Điện tử").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ Chế tạo máy").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ Chế tạo máy Việt – Nhật").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ kỹ thuật Cơ khí").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ kỹ thuật Ô tô").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ Kỹ thuật Cơ điện tử").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ Kỹ thuật Nhiệt").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ Kỹ thuật In").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ Kỹ Thuật Công trình Xây dựng").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ kỹ thuật Môi trường").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ Điều khiển và Tự động hóa").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Công nghệ Kỹ Thuật Hóa Học").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FHQ").build()).name("Ngành Thương mại Điện tử").build(),

                Major.builder().faculty(Faculty.builder().facultyId("FIE").build()).name("Ngành Công nghệ kỹ thuật điện, điện tử").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FIE").build()).name("Ngành Công nghệ kỹ thuật điện tử, viễn thông").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FIE").build()).name("Ngành Công nghệ kỹ thuật điều khiển và tự động hóa").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FIE").build()).name("Ngành Công nghệ kỹ thuật cơ khí").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FIE").build()).name("Ngành Công nghệ chế tạo máy").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FIE").build()).name("Ngành Công nghệ kỹ thuật cơ điện tử").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FIE").build()).name("Ngành Công nghệ kỹ thuật ô tô").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FIE").build()).name("Ngành Công nghệ kỹ thuật công trình xây dựng").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FIE").build()).name("Ngành Công nghệ thông tin").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FIE").build()).name("Ngành Công nghệ thực phẩm").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FIE").build()).name("Ngành Công nghệ kỹ thuật máy tính").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FIE").build()).name("Ngành Công nghệ Kỹ thuật Nhiệt").build(),
                Major.builder().faculty(Faculty.builder().facultyId("FIE").build()).name("Ngành Quản lý công nghiệp").build()

        );

        initMajors.forEach(major -> {
            if(majorRepository.findByName(major.getName()).isEmpty()) {
                majorRepository.save(major);
            }
        });
    }

    private void createRoles() {
        List<String> roles = List.of("ADMIN", "MANAGER","BUSINESS", "INSTRUCTOR", "STUDENT");

        roles.forEach(role -> {
            if(roleRepository.findByRoleName(role).isEmpty())
                roleRepository.save(Role.builder().roleName(role).build());
        });
    }
}