package com.intern.app.models.dto.datamodel;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class ExtendPageConfig extends PageConfig {
    Map<String, Object> additionalData;
}
