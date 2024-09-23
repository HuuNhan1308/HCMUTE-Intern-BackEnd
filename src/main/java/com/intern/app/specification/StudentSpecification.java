package com.intern.app.specification;

import com.intern.app.models.entity.Student;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {

    public static Specification<Student> hasFullname(String fullname) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("profile").get("fullname"), "%" + fullname + "%");
    }

    public static Specification<Student> hasFacultyId(String facultyId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("faculty").get("facultyId"), facultyId);
    }

    public static Specification<Student> hasMajorId(String majorId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("faculty").get("facultyId"), majorId);
    }
}
