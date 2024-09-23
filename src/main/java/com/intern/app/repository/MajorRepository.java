package com.intern.app.repository;

import com.intern.app.models.entity.Major;
import com.intern.app.models.entity.Profile;
import com.intern.app.models.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, String> {

    Optional<Major> findByName(String name);
}
