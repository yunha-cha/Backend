package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.ChatFile;
import com.wittypuppy.backend.messenger.entity.ChatReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatReadStatusRepository extends JpaRepository<ChatReadStatus, Long> {
}
