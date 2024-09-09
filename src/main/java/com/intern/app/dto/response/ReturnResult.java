package com.intern.app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReturnResult<T> {
    String message;
    @Builder.Default
    int code = 200;
    T result;

}
