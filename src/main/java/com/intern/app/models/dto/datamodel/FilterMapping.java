package com.intern.app.models.dto.datamodel;

import com.intern.app.models.enums.FilterOperator;
import com.intern.app.models.enums.FilterType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterMapping {
    String prop;
    String value;
    FilterType type;
    FilterOperator operator;
}
