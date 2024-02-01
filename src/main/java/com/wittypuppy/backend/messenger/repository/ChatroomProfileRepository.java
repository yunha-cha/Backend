package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.Chatroom;
import com.wittypuppy.backend.messenger.entity.ChatroomProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomProfileRepository extends JpaRepository<ChatroomProfile, Long> {
}
