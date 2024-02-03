package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Messenger_ProfileRepository")
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
