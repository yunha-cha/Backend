package com.wittypuppy.backend.admin.repository;

import com.wittypuppy.backend.admin.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProfileRepository extends JpaRepository<Profile, Long> {

}
