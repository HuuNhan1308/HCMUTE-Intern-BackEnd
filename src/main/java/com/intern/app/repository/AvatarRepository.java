package com.intern.app.repository;

import com.intern.app.models.entity.Avatar;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvatarRepository extends AppRepository<Avatar, String> {
    Optional<Avatar> findByOwnerId(String ownerId);
}
