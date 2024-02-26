package com.wittypuppy.backend.admin.repository;

import com.wittypuppy.backend.admin.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminEmailRepository extends JpaRepository<Email,Long> {
}
