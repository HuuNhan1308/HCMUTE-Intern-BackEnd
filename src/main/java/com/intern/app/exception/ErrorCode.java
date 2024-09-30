package com.intern.app.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định...", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "Tài khoản đã tồn tại", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Tài khoản phải có tối thiểu {min} kí tự", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Mật khẩu phải có tối thiểu {min} ki tự", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "Tài khoản không tồn tại", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Không được phép truy cập", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    INVALID_TOKEN(1009, "Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    STUDENT_NOT_FOUND(1010, "Không tìm thấy học sinh", HttpStatus.NOT_FOUND),
    LOGIN_FAIL_CREDENTIALS(1011, "Tài khoản hoặc mật khẩu không đúng, vui lòng thử lại", HttpStatus.BAD_REQUEST),
    STUDENT_EXISTED_ID(1012, "ID học sinh đã tồn tại, vui lòng đăng nhập", HttpStatus.BAD_REQUEST),
    FACULTY_NOT_EXISTED(1013, "Id khoa không tồn tại", HttpStatus.BAD_REQUEST),
    INVALID_FILE(1014, "File không hợp lệ", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND(1015, "Không tìm thấy", HttpStatus.NOT_FOUND),
    BUSINESS_NOT_FOUND(1016, "Doanh nghiệp không tồn tại", HttpStatus.NOT_FOUND),
    RECRUITMENT_NOT_FOUND(1017, "Bài tuyển dụng không tồn tại", HttpStatus.NOT_FOUND),
    ;

    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
