package com.intern.app.models.dto.datamodel;

import com.intern.app.models.enums.SortOrderType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagedData<T, P> {
    List<T> data = new ArrayList<>();
    P pageConfig;
}

