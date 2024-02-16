package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.ChatReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Messenger_ChatReadStatusRepository")
public interface ChatReadStatusRepository extends JpaRepository<ChatReadStatus, Long> {
    Optional<ChatReadStatus> findByChatCodeAndChatroomMemberCode(Long chatCode, Long chatroomMemberCode);

    Optional<ChatReadStatus> findByChatroomCodeAndChatroomMemberCode(Long chatroomCode, Long chatroomMemberCode);

    List<ChatReadStatus> findAllByChatroomCodeIn(List<Long> chatroomCodeList);
}
