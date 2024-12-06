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
    RecruitmentRepository recruitmentRepository;

    ProfileMapper profileMapper;

    PasswordEncoder passwordEncoder;
    Faker faker = new Faker();
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @PostConstruct
    public void initializeRoles() {

        // ROLE
//        createRoles();

        // PERMISSION
//        createPermissions();

        // BIND ROLE WITH PERMISSION
//        bindRolesPermissions();

        // FACULTY
//        createFaculties();

        // MAJOR
//        createMajors();

        // STUDENT
//        createStudents();

        // ADMIN
//        createProfileIfNotExists("admin", "1", "ADMIN");

        // Business
//        createBusinesses();

        //Recruitment from Business
//        createRecruitments();
//        updateRecruitments();
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
                        .password(this.passwordEncoder.encode(("business" + index)))
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

    private void createPermissions() {
        List<String> permissions = List.of("CREATE_ACCOUNT", "CREATE_MAJOR", "CREATE_FACTULTY", "IMPORT_STUDENT",
                "UPDATE_BUSINESS_PROFILE", "SET_RECRUITMENT_BUSINESS_STATUS", "REQUEST_RECRUITMENT");

        permissions.forEach(permission -> {
             if(permissionRepository.findByName(permission).isEmpty()) {
                 permissionRepository.save(Permission.builder().name(permission).build());
             }
        });
    }

    private void bindRolesPermissions() {
        var roles = roleRepository.findAll();
        var permissions = permissionRepository.findAll();

        roles.forEach(role -> {
            permissions.forEach(permission -> {
                if(rolePermissionRepository.findByRoleAndPermissionAndDeletedFalse(role, permission).isEmpty())
                    rolePermissionRepository.save(RolePermission.builder().role(role).permission(permission).build());
            });
        });
    }

    private void createRecruitments() {
        List<String> recruitmentTitles = List.of(
                "Thực Tập Sinh Java",
                "Nhân Viên Quản Trị Mạng (Thu Nhập 12 - 14M)",
                "Lập Trình Viên Frontend (ReactJS)",
                "Kỹ Sư Phát Triển Phần Mềm (C#)",
                "Nhân Viên Hỗ Trợ Kỹ Thuật",
                "Chuyên Viên Marketing Trực Tuyến",
                "Thực Tập Sinh Phát Triển Ứng Dụng Di Động",
                "Quản Lý Dự Án IT",
                "Nhân Viên Kinh Doanh (Lĩnh Vực Công Nghệ)",
                "Kỹ Sư Mạng (Mức Lương 15 - 20M)",
                "Lập Trình Viên Backend (Node.js)",
                "Chuyên Viên Phân Tích Dữ Liệu",
                "Nhân Viên Bán Hàng Online",
                "Thực Tập Sinh Thiết Kế Đồ Họa",
                "Kỹ Sư Hệ Thống",
                "Chuyên Viên Quản Lý Sản Phẩm",
                "Lập Trình Viên Python",
                "Kỹ Sư Kiểm Thử Phần Mềm",
                "Nhân Viên Tư Vấn Khách Hàng",
                "Chuyên Viên Quảng Cáo Trực Tuyến",
                "Thực Tập Sinh Kế Toán",
                "Kỹ Sư Điện Tử",
                "Nhân Viên Phát Triển Thị Trường",
                "Lập Trình Viên Ruby on Rails",
                "Quản Trị Viên Cơ Sở Dữ Liệu",
                "Kỹ Sư Điện Lạnh",
                "Chuyên Viên SEO",
                "Nhân Viên Hành Chính Nhân Sự",
                "Thực Tập Sinh Quản Trị Mạng",
                "Lập Trình Viên Android",
                "Kỹ Sư Phần Cứng",
                "Nhân Viên Dịch Thuật",
                "Thực Tập Sinh Chăm Sóc Khách Hàng",
                "Kỹ Sư Cơ Khí",
                "Nhân Viên Telesales",
                "Chuyên Viên Sản Phẩm Công Nghệ",
                "Lập Trình Viên PHP",
                "Kỹ Sư Phát Triển Game",
                "Nhân Viên Chăm Sóc Khách Hàng",
                "Chuyên Viên Tư Vấn Tuyển Dụng",
                "Thực Tập Sinh Thiết Kế UX/UI",
                "Lập Trình Viên C/C++",
                "Kỹ Sư Chất Lượng",
                "Nhân Viên Phát Triển Kinh Doanh",
                "Chuyên Viên Quản Lý Rủi Ro",
                "Thực Tập Sinh Phát Triển Web",
                "Kỹ Sư Nguồn Nhân Lực",
                "Nhân Viên Chuyên Viên Dữ Liệu",
                "Chuyên Viên Tư Vấn Tài Chính",
                "Lập Trình Viên Blockchain",
                "Kỹ Sư Thông Tin Địa Lý",
                "Nhân Viên Giao Nhận Hàng Hóa",
                "Chuyên Viên Truyền Thông",
                "Thực Tập Sinh Phân Tích Dữ Liệu",
                "Kỹ Sư Đường Ống",
                "Nhân Viên Chuyên Viên Pháp Lý",
                "Chuyên Viên Kế Toán",
                "Lập Trình Viên C# .NET",
                "Kỹ Sư Thiết Kế Kỹ Thuật",
                "Nhân Viên Phân Tích Tài Chính",
                "Chuyên Viên Tối Ưu Hóa Quá Trình",
                "Thực Tập Sinh Quản Trị Hệ Thống",
                "Kỹ Sư Hệ Thống Thông Tin",
                "Nhân Viên Kinh Doanh Bất Động Sản",
                "Chuyên Viên Hỗ Trợ Kỹ Thuật",
                "Thực Tập Sinh Quản Lý Dự Án",
                "Lập Trình Viên IoT",
                "Kỹ Sư Phát Triển Ứng Dụng Web",
                "Nhân Viên Xử Lý Đơn Hàng",
                "Chuyên Viên Nghiên Cứu Thị Trường",
                "Thực Tập Sinh Quản Lý Tài Chính",
                "Kỹ Sư Vận Hành",
                "Nhân Viên Hỗ Trợ Khách Hàng",
                "Chuyên Viên Phát Triển Chiến Lược",
                "Lập Trình Viên JavaScript",
                "Kỹ Sư Sản Xuất",
                "Nhân Viên Quản Lý Bán Hàng",
                "Chuyên Viên Tư Vấn Chiến Lược",
                "Thực Tập Sinh Đào Tạo",
                "Kỹ Sư Kinh Doanh",
                "Nhân Viên Thiết Kế Đồ Họa",
                "Chuyên Viên Phát Triển Hệ Thống",
                "Thực Tập Sinh Dữ Liệu Lớn (Big Data)",
                "Kỹ Sư Bảo Trì",
                "Nhân Viên Tư Vấn Kinh Doanh",
                "Chuyên Viên Quản Trị Mạng Xã Hội",
                "Thực Tập Sinh Tiếp Thị",
                "Kỹ Sư Công Nghệ Thông Tin",
                "Nhân Viên Kinh Doanh Quốc Tế",
                "Chuyên Viên Kỹ Thuật Phần Mềm",
                "Thực Tập Sinh Hỗ Trợ IT",
                "Chuyên Viên Bảo Mật Thông Tin"
        );

        List<Map<String, String>> recruitmentDetails = List.of(
                Map.of("keyskill", "Java, Spring, Hibernate", "description", "Thực tập sinh cần có kiến thức Java cơ bản, làm quen với Spring và Hibernate."),
                Map.of("keyskill", "Networking, Cisco, Windows Server", "description", "Quản trị mạng, cấu hình thiết bị Cisco và vận hành hệ thống Windows Server."),
                Map.of("keyskill", "ReactJS, HTML, CSS, JavaScript", "description", "Phát triển giao diện người dùng với ReactJS, sử dụng các công nghệ HTML, CSS, JavaScript."),
                Map.of("keyskill", "C#, .NET, SQL Server", "description", "Phát triển phần mềm trên nền tảng .NET với C#, làm việc với cơ sở dữ liệu SQL Server."),
                Map.of("keyskill", "Technical Support, Troubleshooting, Networking", "description", "Hỗ trợ kỹ thuật, xử lý sự cố mạng và hệ thống."),
                Map.of("keyskill", "Digital Marketing, SEO, Google Ads", "description", "Thực hiện các chiến dịch tiếp thị trực tuyến, tối ưu hóa SEO và quản lý quảng cáo Google."),
                Map.of("keyskill", "Flutter, Kotlin, Android, iOS", "description", "Phát triển ứng dụng di động đa nền tảng với Flutter và Kotlin trên Android, iOS."),
                Map.of("keyskill", "Project Management, Agile, Scrum", "description", "Quản lý dự án IT, sử dụng phương pháp Agile hoặc Scrum."),
                Map.of("keyskill", "Sales, Technology Solutions, Negotiation", "description", "Kinh doanh giải pháp công nghệ, phát triển mối quan hệ và đàm phán hợp đồng."),
                Map.of("keyskill", "Networking, Firewall, VPN", "description", "Quản lý hệ thống mạng, thiết lập Firewall và kết nối VPN."),
                Map.of("keyskill", "Node.js, MongoDB, Express.js", "description", "Phát triển backend với Node.js, xây dựng REST API và làm việc với cơ sở dữ liệu MongoDB."),
                Map.of("keyskill", "Data Analysis, Python, SQL", "description", "Phân tích dữ liệu với Python, sử dụng SQL để trích xuất và xử lý dữ liệu."),
                Map.of("keyskill", "E-commerce, Online Sales, Customer Management", "description", "Quản lý bán hàng online, chăm sóc khách hàng và tối ưu hóa hiệu quả thương mại điện tử."),
                Map.of("keyskill", "Adobe Photoshop, Illustrator, Graphic Design", "description", "Thiết kế đồ họa, sử dụng các phần mềm Photoshop và Illustrator."),
                Map.of("keyskill", "Linux, Bash, System Engineering", "description", "Vận hành và bảo trì hệ thống Linux, tự động hóa quy trình với Bash."),
                Map.of("keyskill", "Product Management, Roadmap Planning, UX Design", "description", "Quản lý sản phẩm, lập kế hoạch phát triển sản phẩm và tối ưu UX."),
                Map.of("keyskill", "Python, Flask, Django", "description", "Phát triển ứng dụng web với Python, sử dụng Flask hoặc Django."),
                Map.of("keyskill", "Software Testing, Selenium, JUnit", "description", "Thực hiện kiểm thử phần mềm với Selenium và các công cụ kiểm thử tự động."),
                Map.of("keyskill", "Customer Service, Communication, CRM", "description", "Hỗ trợ khách hàng, giao tiếp hiệu quả và quản lý dữ liệu khách hàng qua CRM."),
                Map.of("keyskill", "Google Ads, Facebook Ads, Content Creation", "description", "Tạo và quản lý quảng cáo trên Google, Facebook, xây dựng nội dung tiếp thị."),
                Map.of("keyskill", "Accounting, Financial Analysis, Excel", "description", "Thực tập sinh cần kiến thức cơ bản về kế toán và phân tích tài chính, sử dụng Excel."),
                Map.of("keyskill", "Electronics, Circuit Design, PCB Testing", "description", "Làm việc với thiết kế mạch điện tử, kiểm tra PCB và hệ thống nhúng."),
                Map.of("keyskill", "Market Research, Business Development, Negotiation", "description", "Nghiên cứu thị trường, phát triển kinh doanh và kỹ năng đàm phán."),
                Map.of("keyskill", "Ruby on Rails, PostgreSQL, Web Development", "description", "Phát triển ứng dụng web với Ruby on Rails, sử dụng cơ sở dữ liệu PostgreSQL."),
                Map.of("keyskill", "Database Administration, MySQL, Query Optimization", "description", "Quản trị cơ sở dữ liệu MySQL, tối ưu hóa truy vấn và hiệu suất."),
                Map.of("keyskill", "HVAC Systems, Maintenance, Automation", "description", "Thiết kế và bảo trì hệ thống HVAC, tích hợp tự động hóa."),
                Map.of("keyskill", "SEO, SEM, Keyword Research", "description", "Tối ưu hóa SEO/SEM, nghiên cứu từ khóa và cải thiện hiệu suất website."),
                Map.of("keyskill", "Human Resources, Recruitment, Training", "description", "Hỗ trợ quản lý nhân sự, tuyển dụng và đào tạo nhân viên."),
                Map.of("keyskill", "Networking, CCNA, Firewall", "description", "Quản trị mạng với chứng chỉ CCNA, cấu hình Firewall."),
                Map.of("keyskill", "Android, Kotlin, Firebase", "description", "Phát triển ứng dụng Android với Kotlin, tích hợp Firebase."),
                Map.of("keyskill", "Translation, Localization, Technical Writing", "description", "Dịch thuật, bản địa hóa và soạn thảo tài liệu kỹ thuật."),
                Map.of("keyskill", "Cybersecurity, Penetration Testing, Network Security", "description", "Bảo mật thông tin hệ thống, thực hiện kiểm thử xâm nhập và bảo vệ an ninh mạng."),
                Map.of("keyskill", "Customer Service, Communication, Problem Solving", "description", "Thực tập sinh hỗ trợ khách hàng, cải thiện trải nghiệm người dùng và giải quyết vấn đề."),
                Map.of("keyskill", "Mechanical Engineering, CAD, Maintenance", "description", "Thiết kế cơ khí, sử dụng phần mềm CAD và bảo trì thiết bị."),
                Map.of("keyskill", "Telesales, CRM, Communication Skills", "description", "Bán hàng qua điện thoại, sử dụng hệ thống CRM và giao tiếp hiệu quả."),
                Map.of("keyskill", "Product Management, Technology, Market Analysis", "description", "Quản lý sản phẩm công nghệ, phân tích thị trường và phát triển sản phẩm."),
                Map.of("keyskill", "PHP, Laravel, MySQL", "description", "Phát triển ứng dụng web với PHP, sử dụng framework Laravel và cơ sở dữ liệu MySQL."),
                Map.of("keyskill", "Game Development, Unity, Unreal Engine", "description", "Phát triển trò chơi sử dụng Unity hoặc Unreal Engine, thiết kế và lập trình game logic."),
                Map.of("keyskill", "Customer Care, Problem Resolution, CRM", "description", "Chăm sóc khách hàng, giải quyết khiếu nại và tối ưu hóa hệ thống CRM."),
                Map.of("keyskill", "Recruitment, Talent Acquisition, Communication", "description", "Tư vấn tuyển dụng, tìm kiếm tài năng và giao tiếp hiệu quả với ứng viên."),
                Map.of("keyskill", "UX/UI Design, Figma, Prototyping", "description", "Thiết kế UX/UI, sử dụng Figma để tạo prototype và cải thiện trải nghiệm người dùng."),
                Map.of("keyskill", "C, C++, Embedded Systems", "description", "Phát triển ứng dụng nhúng với ngôn ngữ C/C++, tối ưu hóa hiệu năng phần mềm."),
                Map.of("keyskill", "Quality Assurance, Testing, Six Sigma", "description", "Đảm bảo chất lượng sản phẩm, kiểm thử và áp dụng quy trình Six Sigma."),
                Map.of("keyskill", "Business Development, Negotiation, Market Research", "description", "Phát triển kinh doanh, đàm phán và nghiên cứu thị trường."),
                Map.of("keyskill", "Risk Management, Compliance, Risk Assessment", "description", "Quản lý rủi ro, đánh giá và đảm bảo tuân thủ các quy định."),
                Map.of("keyskill", "HTML, CSS, JavaScript, Web Development", "description", "Phát triển web với HTML, CSS và JavaScript, tối ưu hóa hiệu năng website."),
                Map.of("keyskill", "Human Resources, Recruitment, Training", "description", "Quản lý nguồn nhân lực, tuyển dụng và đào tạo nhân viên."),
                Map.of("keyskill", "Data Management, SQL, Data Analysis", "description", "Quản lý dữ liệu, phân tích và tối ưu hóa cơ sở dữ liệu."),
                Map.of("keyskill", "Financial Planning, Investment Advisory, Excel", "description", "Tư vấn tài chính, lập kế hoạch đầu tư và sử dụng Excel chuyên nghiệp."),
                Map.of("keyskill", "Blockchain, Smart Contracts, Solidity", "description", "Phát triển ứng dụng Blockchain, viết hợp đồng thông minh với Solidity."),
                Map.of("keyskill", "GIS, Spatial Analysis, Remote Sensing", "description", "Kỹ sư GIS làm việc với phân tích không gian và cảm biến từ xa."),
                Map.of("keyskill", "Logistics, Delivery, Inventory Management", "description", "Giao nhận hàng hóa, quản lý tồn kho và vận chuyển."),
                Map.of("keyskill", "Public Relations, Social Media, Content Creation", "description", "Quản lý truyền thông, tạo nội dung và tương tác trên mạng xã hội."),
                Map.of("keyskill", "Data Analysis, Python, Machine Learning", "description", "Phân tích dữ liệu, sử dụng Python và các mô hình học máy."),
                Map.of("keyskill", "Pipeline Engineering, CAD, Maintenance", "description", "Thiết kế và bảo trì đường ống, sử dụng phần mềm CAD."),
                Map.of("keyskill", "Legal Research, Contract Drafting, Compliance", "description", "Tư vấn pháp lý, soạn thảo hợp đồng và đảm bảo tuân thủ."),
                Map.of("keyskill", "Accounting, Taxation, Financial Reporting", "description", "Quản lý kế toán, báo cáo tài chính và nộp thuế."),
                Map.of("keyskill", "C#, .NET, Entity Framework", "description", "Phát triển ứng dụng với C#, sử dụng .NET Framework và Entity Framework."),
                Map.of("keyskill", "Technical Design, CAD, Engineering Analysis", "description", "Kỹ sư thiết kế kỹ thuật, làm việc với CAD và phân tích kỹ thuật."),
                Map.of("keyskill", "Financial Analysis, Budgeting, Forecasting", "description", "Phân tích tài chính, lập ngân sách và dự đoán kinh doanh."),
                Map.of("keyskill", "Process Optimization, Lean Manufacturing, Automation", "description", "Tối ưu hóa quy trình, sản xuất Lean và tự động hóa."),
                Map.of("keyskill", "System Administration, Linux, Windows Server", "description", "Quản trị hệ thống, vận hành Linux và Windows Server."),
                Map.of("keyskill", "Information Systems, Data Management, Cloud Computing", "description", "Kỹ sư hệ thống thông tin, quản lý dữ liệu và sử dụng điện toán đám mây."),
                Map.of("keyskill", "Real Estate, Sales, Negotiation", "description", "Kinh doanh bất động sản, phát triển khách hàng và đàm phán hợp đồng."),
                Map.of("keyskill", "Technical Support, Troubleshooting, Networking", "description", "Hỗ trợ kỹ thuật, xử lý sự cố và quản lý hệ thống mạng."),
                Map.of("keyskill", "Project Management, Agile, Scheduling", "description", "Thực tập sinh quản lý dự án, lập kế hoạch và sử dụng phương pháp Agile."),
                Map.of("keyskill", "IoT Development, Embedded Systems, Sensors", "description", "Lập trình IoT, làm việc với hệ thống nhúng và cảm biến."),
                Map.of("keyskill", "Web Development, JavaScript, HTML, CSS", "description", "Phát triển ứng dụng web, tối ưu hóa giao diện người dùng và xây dựng các tính năng tương tác."),
                Map.of("keyskill", "Order Processing, Customer Service, ERP", "description", "Xử lý đơn hàng, chăm sóc khách hàng và quản lý dữ liệu trên hệ thống ERP."),
                Map.of("keyskill", "Market Research, Data Analysis, Communication", "description", "Nghiên cứu thị trường, phân tích dữ liệu và trình bày kết quả."),
                Map.of("keyskill", "Financial Management, Budgeting, Reporting", "description", "Quản lý tài chính, lập ngân sách và phân tích báo cáo."),
                Map.of("keyskill", "Operations Management, Process Optimization, Troubleshooting", "description", "Vận hành hệ thống, tối ưu hóa quy trình và xử lý sự cố."),
                Map.of("keyskill", "Customer Support, CRM, Problem Solving", "description", "Hỗ trợ khách hàng, sử dụng CRM và giải quyết vấn đề kịp thời."),
                Map.of("keyskill", "Strategic Planning, Business Development, Market Analysis", "description", "Phát triển chiến lược, phân tích thị trường và đưa ra kế hoạch kinh doanh."),
                Map.of("keyskill", "JavaScript, Node.js, React.js", "description", "Phát triển ứng dụng với JavaScript, xây dựng giao diện người dùng và backend."),
                Map.of("keyskill", "Manufacturing, Production Planning, Quality Control", "description", "Kỹ sư sản xuất, lên kế hoạch và đảm bảo chất lượng sản phẩm."),
                Map.of("keyskill", "Sales Management, Team Leadership, CRM", "description", "Quản lý bán hàng, dẫn dắt đội nhóm và tối ưu hệ thống CRM."),
                Map.of("keyskill", "Strategic Consulting, Problem Solving, Business Analysis", "description", "Tư vấn chiến lược, giải quyết vấn đề và phân tích kinh doanh."),
                Map.of("keyskill", "Training Development, HR, Communication", "description", "Đào tạo, phát triển nhân sự và cải thiện kỹ năng giao tiếp."),
                Map.of("keyskill", "Sales Engineering, Technical Knowledge, Negotiation", "description", "Kỹ sư kinh doanh, hỗ trợ kỹ thuật và đàm phán với khách hàng."),
                Map.of("keyskill", "Graphic Design, Adobe Photoshop, Creativity", "description", "Thiết kế đồ họa, sử dụng Adobe Photoshop và phát triển ý tưởng sáng tạo."),
                Map.of("keyskill", "System Development, DevOps, Cloud Computing", "description", "Phát triển hệ thống, sử dụng DevOps và triển khai trên môi trường đám mây."),
                Map.of("keyskill", "Big Data, Hadoop, Spark", "description", "Phân tích dữ liệu lớn, sử dụng Hadoop và Apache Spark."),
                Map.of("keyskill", "Maintenance Engineering, Equipment Troubleshooting, Preventive Maintenance", "description", "Bảo trì thiết bị, xử lý sự cố và thực hiện bảo dưỡng định kỳ."),
                Map.of("keyskill", "Business Consulting, Customer Acquisition, Negotiation", "description", "Tư vấn kinh doanh, tìm kiếm khách hàng và đàm phán hợp đồng."),
                Map.of("keyskill", "Social Media Management, Content Creation, Analytics", "description", "Quản trị mạng xã hội, sáng tạo nội dung và phân tích hiệu quả."),
                Map.of("keyskill", "Marketing, Campaign Planning, Market Research", "description", "Thực tập sinh tiếp thị, hỗ trợ lên kế hoạch và nghiên cứu thị trường."),
                Map.of("keyskill", "IT Infrastructure, System Administration, Troubleshooting", "description", "Kỹ sư CNTT, quản trị hệ thống và xử lý các vấn đề kỹ thuật."),
                Map.of("keyskill", "International Sales, Cross-Cultural Communication, Negotiation", "description", "Kinh doanh quốc tế, giao tiếp đa văn hóa và đàm phán hợp đồng."),
                Map.of("keyskill", "Software Engineering, Coding, Debugging", "description", "Kỹ thuật phần mềm, phát triển ứng dụng và sửa lỗi phần mềm."),
                Map.of("keyskill", "IT Support, Troubleshooting, System Monitoring", "description", "Thực tập sinh hỗ trợ IT, giám sát hệ thống và xử lý sự cố."),
                Map.of("keyskill", "Information Security, Cybersecurity, Risk Assessment, Penetration Testing, Network Security", "description", "Đảm bảo an ninh thông tin, thực hiện đánh giá rủi ro, kiểm tra thâm nhập và triển khai các biện pháp bảo mật mạng.")
        );

        List<Business> allBusinesses = businessRepository.findAll();

        for (int i = 0; i < recruitmentTitles.size(); i++) {
            if(recruitmentRepository.findByTitle(recruitmentTitles.get(i)).isEmpty()) {
                Business randomBusiness = allBusinesses.get(new Random().nextInt(allBusinesses.size()));

                Recruitment recruitment = Recruitment.builder()
                        .title(recruitmentTitles.get(i))
                        .description(recruitmentDetails.get(i).get("description"))
                        .location(faker.address().streetAddress(true))
                        .type("Part time")
                        .keySkills(recruitmentDetails.get(i).get("keyskill"))
                        .position("Thực tập sinh")
                        .workingDay("Thứ 2 - Thứ 6")
                        .workingHour("9:00 - 18:30")
                        .business(randomBusiness)
                        .build();

                recruitmentRepository.save(recruitment);
            }
        }


    }
}