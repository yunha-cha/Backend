package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.ChatFile;
import com.wittypuppy.backend.messenger.entity.ChatReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Messenger_ChatReadStatusRepository")
public interface ChatReadStatusRepository extends JpaRepository<ChatReadStatus, Long> {
}
