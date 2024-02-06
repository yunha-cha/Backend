package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.Chatroom;
import com.wittypuppy.backend.messenger.entity.ChatroomProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Messenger_ChatroomProfileRepository")
public interface ChatroomProfileRepository extends JpaRepository<ChatroomProfile, Long> {
    Optional<ChatroomProfile> findFirstByChatroomCodeOrderByChatroomProfileRegistDateDesc(Long chatroomCode);
}
