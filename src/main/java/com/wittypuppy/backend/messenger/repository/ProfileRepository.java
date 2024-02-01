package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.Messenger;
import com.wittypuppy.backend.messenger.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
