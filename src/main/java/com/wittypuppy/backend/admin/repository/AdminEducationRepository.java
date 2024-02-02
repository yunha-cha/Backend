package com.wittypuppy.backend.admin.repository;

import com.wittypuppy.backend.admin.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminEducationRepository extends JpaRepository<Education, Long> {

}
