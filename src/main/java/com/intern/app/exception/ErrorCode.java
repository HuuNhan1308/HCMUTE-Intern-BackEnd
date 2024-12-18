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
    FACULTY_NOT_EXISTED(1013, "Id khoa không tồn tại", HttpStatus.NOT_FOUND),
    INVALID_FILE(1014, "File không hợp lệ", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND(1015, "Không tìm thấy", HttpStatus.NOT_FOUND),
    BUSINESS_NOT_FOUND(1016, "Doanh nghiệp không tồn tại", HttpStatus.NOT_FOUND),
    RECRUITMENT_NOT_FOUND(1017, "Bài tuyển dụng không tồn tại", HttpStatus.NOT_FOUND),
    PERMISSION_EXISTED(1017, "Quyền đã tồn tại", HttpStatus.BAD_REQUEST),
    RECRUITMENT_REQUEST_NOT_EXIST(1018, "Bài tuyển dụng không tồn tại", HttpStatus.BAD_REQUEST),
    NOT_EXCEL_FORMAT(1019, "Định dạng nội dung không phải excel, vui lòng kiểm tra lại", HttpStatus.BAD_REQUEST),
    INSTRUCTOR_NOT_FOUND(1020, "Không tìm thấy giảng viên...", HttpStatus.BAD_REQUEST),
    INSTRUCTOR_REQUEST_NOT_FOUND(1021, "Yêu cầu không tồn tại", HttpStatus.NOT_FOUND),
    ROLE_NOT_EXIST(1022, "Role không tồn tại", HttpStatus.NOT_FOUND),
    PERMISSION_NOT_EXIST(1022, "Permission không tồn tại", HttpStatus.NOT_FOUND),
    INVALID_NUMERIC_VALUE(1023, "Định dạng không phải numeric, vui lòng thử lại", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_FILTER_OPERATOR(1024, "Kiểu filter không được hỗ trợ", HttpStatus.BAD_REQUEST),
    NOTIFICATION_NOT_FOUND(1025, "Notification không tồn tại", HttpStatus.BAD_REQUEST),
    MAJOR_NOT_EXISTED(1026, "Ngành không tồn tại", HttpStatus.NOT_FOUND),
    AVATAR_NOT_EXISTED(1027, "Avatar không tồn tại hoặc đã xảy ra lỗi", HttpStatus.NOT_FOUND),
    STUDENT_HAVE_NO_COMPLETED_RECRUITMENT(1028, "Có sinh viên chưa hoàn thành kì thực tập, vui lòng kiểm tra lại danh sách đã chọn", HttpStatus.BAD_REQUEST),
    INVALID_BOOLEAN_VALUE(1029, "Định dạng không phải boolean, vui lòng thử lại", HttpStatus.BAD_REQUEST),
    NOT_PENDING_REQUEST(1030, "Bạn chỉ được phép xoá những yêu cầu đang chờ xét duyệt", HttpStatus.BAD_REQUEST),
    INVALID_FILE_TYPE(1031, "Bạn chỉ được phép upload CV có định dạng PDF", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(1032, "Dung lượng tối đa để upload là 5MB, vui lòng kiểm trả lại dung lượng", HttpStatus.BAD_REQUEST),
    INVALID_FORMAT_PHONENUMBER(1033, "Định dạng số điện thoại không đúng, vui lòng kiểm tra lại", HttpStatus.BAD_REQUEST),
    ;

    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
