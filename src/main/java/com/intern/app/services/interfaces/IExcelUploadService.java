package com.intern.app.services.interfaces;

import com.intern.app.models.dto.response.ReturnResult;
import com.intern.app.models.entity.Student;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public interface IExcelUploadService {
    boolean IsValidExcelFile(MultipartFile file);
    List<Student> GetStudentDataFromExcel(InputStream inputStream);
    ReturnResult<Boolean> ImportStudents(MultipartFile file);
}
