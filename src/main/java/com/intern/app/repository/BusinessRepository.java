package com.intern.app.repository;

import com.intern.app.models.entity.Business;
import com.intern.app.models.entity.Profile;
import com.intern.app.models.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, String> {

}
