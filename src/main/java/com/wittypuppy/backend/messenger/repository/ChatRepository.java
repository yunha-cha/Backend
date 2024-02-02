package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("Messenger_ChatRepository")
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByChatroomCodeAndChatroomMemberCode(Long chatroomCode,Long chatroomMemberCode);
}
