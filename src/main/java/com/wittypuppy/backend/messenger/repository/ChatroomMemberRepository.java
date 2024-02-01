package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.ChatFile;
import com.wittypuppy.backend.messenger.entity.ChatroomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Messenger_ChatroomMemberRepository")
public interface ChatroomMemberRepository extends JpaRepository<ChatroomMember, Long> {
}
