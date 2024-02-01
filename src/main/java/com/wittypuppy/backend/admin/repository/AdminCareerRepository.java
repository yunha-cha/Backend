package com.wittypuppy.backend.admin.repository;

import com.wittypuppy.backend.admin.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminCareerRepository extends JpaRepository<Career, Long> {

}
