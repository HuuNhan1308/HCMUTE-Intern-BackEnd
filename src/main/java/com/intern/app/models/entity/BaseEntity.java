package com.intern.app.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@MappedSuperclass
public class BaseEntity {

    LocalDateTime dateCreated;
    LocalDateTime dateModified;
    LocalDateTime dateDeleted;
    Boolean deleted;

    @PrePersist
    protected void onCreate() {
        dateCreated = LocalDateTime.now();
        dateModified = LocalDateTime.now();
        deleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        dateModified = LocalDateTime.now();
    }

    @PreRemove
    protected void onDelete() {
        dateModified = LocalDateTime.now();
        dateDeleted = LocalDateTime.now();
        deleted = true;
    }
}
