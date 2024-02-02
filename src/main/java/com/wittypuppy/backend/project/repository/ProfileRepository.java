package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Project_ProfileRepository")
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}