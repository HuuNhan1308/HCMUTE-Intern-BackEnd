package com.intern.app.repository;

import com.intern.app.models.entity.UploadContent;

import java.util.Optional;

import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadContentRepository extends AppRepository<UploadContent, String> {
    Optional<UploadContent> findByUploadContentId(String uploadContentId);
}