package com.intern.app.repository;

import com.intern.app.models.entity.UploadContent;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadContentRepository extends JpaRepository<UploadContent, String> {
    Optional<UploadContent> findByUploadContentId(String uploadContentId);
}