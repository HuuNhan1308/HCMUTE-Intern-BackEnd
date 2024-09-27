package com.intern.app.models.dto.datamodel;

import com.intern.app.models.enums.SortOrderType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderMapping {
    String sort;
    SortOrderType sortOrderType;
}
