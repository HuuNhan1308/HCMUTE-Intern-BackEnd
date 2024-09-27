package com.intern.app.models.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
