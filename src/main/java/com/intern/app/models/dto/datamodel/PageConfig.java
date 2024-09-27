package com.intern.app.models.dto.datamodel;

import com.intern.app.models.entity.Profile;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class PageConfig {
    int totalPage;
    int totalRecords;
    int currentPage;
    int pageSize;
    List<OrderMapping> orders;

    // Method to build Sort for multiple fields
    public Sort getSort() {
        List<Sort.Order> sortOrders = new ArrayList<>();
        for (OrderMapping order : orders) {
            String direction = order.getSortOrderType().toString().toUpperCase();
            Sort.Order sortOrder = new Sort.Order(Sort.Direction.fromString(direction), order.getSort());
            sortOrders.add(sortOrder);
        }
        return Sort.by(sortOrders);
    }
}

