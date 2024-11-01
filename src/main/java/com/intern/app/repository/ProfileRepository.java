package com.intern.app.repository;

import com.intern.app.models.entity.Profile;
import com.intern.app.repository.CustomRepository.AppRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends AppRepository<Profile, String> {
    Optional<Profile> findByUsernameAndPassword(String username, String password);

    Optional<Profile> findByUsername(String username);

}
