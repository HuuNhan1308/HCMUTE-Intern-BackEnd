package com.intern.app.models.dto.datamodel;

import com.intern.app.exception.AppException;
import com.intern.app.exception.ErrorCode;
import com.intern.app.models.entity.BaseEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FilterSpecification<T extends BaseEntity> {

    public Specification<T> GetSearchSpecification(List<FilterMapping> filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            filters.forEach(filter -> {
                switch (filter.getType()) {
                    case TEXT -> {
                        Predicate textPredicate = this.HandleTextPredicate(filter, root, criteriaBuilder);
                        predicates.add(textPredicate);
                    }
                    case NUMBER -> {
                        Predicate numberPredicate = this.HandleNumberPredicate(filter, root, criteriaBuilder);
                        predicates.add(numberPredicate);
                    }
                    case null, default -> {}
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Predicate HandleNumberPredicate(FilterMapping filter, Root<T> root, CriteriaBuilder criteriaBuilder) {
        List<String> keys = new ArrayList<>(List.of(filter.getProp().split("\\.")));
        Join<BaseEntity, BaseEntity> join = null;
        String finalProp = keys.removeLast(); // Get the last property in the path

        // Navigate joins if nested properties are present
        if (!keys.isEmpty()) {
            join = root.join(keys.removeFirst());
            for (String key : keys) {
                join = join.join(key);
            }
        }

        Path<Number> objectPath = join != null ? join.get(finalProp) : root.get(finalProp);

        // Parse the number value based on the expected data type
        Number value;
        try {
            value = Double.valueOf(filter.getValue());  // Adjust this for a specific number type if needed
        } catch (NumberFormatException e) {
            throw new AppException(ErrorCode.INVALID_NUMERIC_VALUE);
        }

        // Get the predicate based on the operator
        return getNumberPredicate(filter, criteriaBuilder, objectPath, value);
    }

    // Helper method to build the predicate based on the operator
    private Predicate getNumberPredicate(FilterMapping filter, CriteriaBuilder criteriaBuilder, Path<Number> objectPath, Number value) {
        switch (filter.getOperator()) {
            case EQUALS -> {
                return criteriaBuilder.equal(objectPath, value);
            }
            case GREATER_THAN -> {
                return criteriaBuilder.gt(objectPath.as(Double.class), value.doubleValue());
            }
            case GREATER_THAN_OR_EQUAL -> {
                return criteriaBuilder.ge(objectPath.as(Double.class), value.doubleValue());
            }
            case LESS_THAN -> {
                return criteriaBuilder.lt(objectPath.as(Double.class), value.doubleValue());
            }
            case LESS_THAN_OR_EQUAL -> {
                return criteriaBuilder.le(objectPath.as(Double.class), value.doubleValue());
            }
            default -> throw new AppException(ErrorCode.UNSUPPORTED_FILTER_OPERATOR);
        }
    }


    public Predicate HandleTextPredicate(FilterMapping filter, Root<T> root, CriteriaBuilder criteriaBuilder) {
        List<String> keys = new ArrayList<>(List.of(filter.getProp().split("\\.")));
        Join<BaseEntity, BaseEntity> join = null;
        String finalProp = keys.removeLast();

        if(!keys.isEmpty()) {
            join = root.join(keys.removeFirst());
            for (String key : keys) {
                join = join.join(key);
            }

            return getTextPredicate(filter, criteriaBuilder, join.get(finalProp));
        }
        else {
            return getTextPredicate(filter, criteriaBuilder, root.get(finalProp));
        }
    }

    private Predicate getTextPredicate(FilterMapping filter, CriteriaBuilder criteriaBuilder, Path<String> objectPath) {
        switch (filter.getOperator()) {
            case CONTAINS -> {
                return criteriaBuilder.like(criteriaBuilder.lower(objectPath), "%" + filter.getValue().toLowerCase() + "%");
            }
            case END_WITH -> {
                return criteriaBuilder.like(criteriaBuilder.lower(objectPath), "%" + filter.getValue().toLowerCase());
            }
            case START_WITH -> {
                return criteriaBuilder.like(criteriaBuilder.lower(objectPath), filter.getValue().toLowerCase() + "%");
            }
            case EQUALS -> {
                return criteriaBuilder.equal(criteriaBuilder.lower(objectPath), filter.getValue());
            }
            case null, default -> throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
