package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.ChatFile;
import com.wittypuppy.backend.messenger.entity.ChatroomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomMemberRepository extends JpaRepository<ChatroomMember, Long> {
}
