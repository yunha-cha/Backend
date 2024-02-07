package com.wittypuppy.backend.calendar.repository;

import com.wittypuppy.backend.calendar.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Calendar_ProfileRepository")
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
