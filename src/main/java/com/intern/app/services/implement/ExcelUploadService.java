package com.intern.app.services.implement;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Faculty;
import com.intern.app.models.entity.Major;
import com.intern.app.models.entity.Profile;
import com.intern.app.models.entity.Student;
import com.intern.app.repository.*;
import com.intern.app.services.interfaces.IExcelUploadService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@EnableMethodSecurity
public class ExcelUploadService implements IExcelUploadService {
    private final FacultyRepository facultyRepository;
    private final MajorRepository majorRepository;
    private final ProfileRepository profileRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public boolean IsValidExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public List<Student> GetStudentDataFromExcel(InputStream inputStream) {
        List<Student> studentList = new ArrayList<>();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Responses");

            int rowIndex = 0;
            for (Row row : sheet) {

                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }

                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                Profile profile = new Profile();
                Student student = new Student();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cellIndex) {
                        case 1 -> {
                            // Tên
                            profile.setFullname(cell.getStringCellValue());
                        }
                        case 2 -> {
                            // MSSV
                            String studentId = cell.getStringCellValue();
                            String studentMail = studentId + "@student.hcmute.edu.vn";
                            Integer year = Integer.valueOf("20" + studentId.substring(0, 2));

                            profile.setUsername(studentId);
                            profile.setPassword(passwordEncoder.encode(studentId));
                            profile.setEmail(studentMail);
                            profile.setRole(roleRepository.findByRoleName("STUDENT").get());

                            student.setStudentId(studentId);
                            student.setYear(year);
                        }
                        case 3 -> {
                            // Giới tính
                            if(cell.getStringCellValue().equals("Nam"))
                                profile.setIsMale(true);
                            else profile.setIsMale(false);
                        }
                        case 4 -> {
                            // SĐT
                            profile.setPhoneNumber(cell.getStringCellValue());
                        }
                        case 5 -> {
                            // Khoa
                            Faculty faculty = facultyRepository.findByName(cell.getStringCellValue())
                                    .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
                            student.setFaculty(faculty);
                        }
                        case 6 -> {
                            // Ngành
                            Major major = majorRepository.findByName(cell.getStringCellValue())
                                    .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
                            student.setMajor(major);
                        }
                        default -> {}
                    }

                    cellIndex++;
                }


                if(profileRepository.findByUsername(profile.getUsername()).isPresent()) {
                    continue;
                }
                student.setProfile(profile);
                studentList.add(student);
            }

            return studentList;
        } catch (IOException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @PreAuthorize("hasAuthority('IMPORT_STUDENT')")
    public ReturnResult<Boolean> ImportStudents(MultipartFile file) {
        var result = new ReturnResult<Boolean>();

        if(!this.IsValidExcelFile(file)){
           throw new AppException(ErrorCode.NOT_EXCEL_FORMAT);
        }

        try {
            List<Student> students = this.GetStudentDataFromExcel(file.getInputStream());
            List<Profile> profiles = students.stream().map((Student::getProfile)).toList();
            profileRepository.saveAll(profiles);
            studentRepository.saveAll(students);

            result.setResult(Boolean.TRUE);
            return result;

        } catch (IOException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
