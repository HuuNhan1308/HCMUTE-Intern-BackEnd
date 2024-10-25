package com.intern.app.repository.CustomRepository;

import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.hibernate.Session;

@Aspect
@Component
public class DeletedFilterAspect {
    private final EntityManager entityManager;

    public DeletedFilterAspect(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before("execution(* com.intern.app.repository.CustomRepository.AppRepository+.*(..))")
    public void enableDeletedFilter() {
        Session session = entityManager.unwrap(Session.class);
        org.hibernate.Filter filter = session.enableFilter("deletedFilter");
        filter.setParameter("deleted", false);
    }
}
