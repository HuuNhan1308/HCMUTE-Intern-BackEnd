package com.intern.app.repository.CustomRepository;

import com.intern.app.models.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class AppRepositoryImpl <T extends BaseEntity, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
        implements AppRepository<T, ID> {

    private final EntityManager entityManager;

    AppRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
                     EntityManager entityManager) {
        super(entityInformation, entityManager);

        // Keep the EntityManager around to used from the newly introduced methods.
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void softDelete(T entity) {
        entity.setDeleted(true);
        entity.setDateDeleted(LocalDateTime.now());
        entity.setDateModified(LocalDateTime.now());
        entityManager.merge(entity);
    }

    @Override
    @Transactional
    public void softDeleteRange(List<T> entities) {
        for (T entity : entities) {
            this.softDelete(entity);
        }
    }

    @Override
    public void enableDeletedFilter() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedFilter").setParameter("isDeleted", false);
    }

}

