package com.intern.app.models.dto.datamodel;

import com.fasterxml.jackson.databind.ser.Serializers;
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
                    case NUMBER -> {}
                    case null, default -> {}
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
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
            case null, default -> throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
