package com.intern.app.models.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecruitmentRequestGrading {

    String recruitmentRequestId;

    @NotNull(message = "Điểm số không thể để trống")
    @Min(value = 0, message = "Điểm phải lớn hơn hoặc bằng 0")
    @Max(value = 10, message = "Điểm phải nhỏ hơn hoặc bằng 10")
    Double point;

    @NotNull(message = "Vui lòng đánh giá học sinh")
    String messageToStudent;
}
