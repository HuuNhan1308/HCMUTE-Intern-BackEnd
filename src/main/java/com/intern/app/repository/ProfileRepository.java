package com.intern.app.repository;

import com.intern.app.models.entity.Profile;
import org.checkerframework.checker.optional.qual.OptionalPropagator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {
    Optional<Profile> findByUsernameAndPassword(String username, String password);

    Optional<Profile> findByUsernameAndDeletedFalse(String username);

    Optional<Profile> findByUsername(String username);
}
