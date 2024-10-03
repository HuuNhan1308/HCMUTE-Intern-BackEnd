package com.intern.app.repository.CustomRepository;

import com.intern.app.models.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface AppRepository<T extends BaseEntity, ID extends Serializable> extends JpaRepository<T, ID> {
    void softDelete(T entity);
    void softDeleteRange(List<T> entities);
}
