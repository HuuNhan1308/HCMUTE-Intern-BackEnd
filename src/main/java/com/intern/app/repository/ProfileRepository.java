package com.intern.app.repository;

import com.intern.app.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {
    Profile findByUsernameAndPassword(String username, String password);

    Optional<Profile> findByUsername(String username);
}
