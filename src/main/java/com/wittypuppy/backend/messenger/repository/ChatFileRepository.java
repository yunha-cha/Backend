package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.Chat;
import com.wittypuppy.backend.messenger.entity.ChatFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Messenger_ChatFileRepository")
public interface ChatFileRepository extends JpaRepository<ChatFile, Long> {
}
