package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.ChatFile;
import com.wittypuppy.backend.messenger.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Messenger_ChatroomRepository")
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
}
