package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByChatroomCodeAndChatContentLike(Long chatroomCode, String searchValue);
    List<Chat> findAllByChatroomCode(Long chatroomCode);
}
