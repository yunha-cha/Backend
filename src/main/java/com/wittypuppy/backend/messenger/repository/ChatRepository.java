package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.dto.RecentChatInterface;
import com.wittypuppy.backend.messenger.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Messenger_ChatRepository")
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query(value =
            "SELECT " +
                    "tc.chat_code chatCode," +
                    "tcrs.chat_code userChatCode " +
                    "FROM tbl_chatroom tcr " +
                    "JOIN tbl_chat tc ON tcr.chatroom_code = tc.chatroom_code " +
                    "LEFT JOIN tbl_chatroom_member tcrm ON tcr.chatroom_code = tcrm.chatroom_code " +
                    "RIGHT JOIN tbl_chat_read_status tcrs ON tcrm.chatroom_member_code = tcrs.chatroom_member_code " +
                    "LEFT JOIN tbl_employee te ON tcrm.employee_code = te.employee_code " +
                    "WHERE te.employee_code = :userEmployeeCode " +
                    "AND tc.chat_code = (SELECT MAX(chat_code) FROM tbl_chat tc2 WHERE tc2.chat_code = tc.chat_code) " +
                    "OR tc.chat_code is null " +
                    "AND tcrs.chatroom_code = :chatroomCode " +
                    "ORDER BY tc.chat_code is not null " +
                    "LIMIT 1"
            , nativeQuery = true)
    RecentChatInterface findChatCodesInChatAndChatReadStatus(Long chatroomCode, Long userEmployeeCode);

    Optional<Chat> findFirstByChatroomCodeOrderByChatCodeDesc(Long chatroomCode);

    List<Chat> findAllByChatroomCode(Long chatroomCode);

    List<Chat> findAllByChatroomCodeAndChatCodeIsGreaterThan(Long chatroomCode, Long chatCode);

    List<Chat> findAllByChatroomCodeAndChatCodeIsLessThanEqual(Long chatroomCode, Long chatCode, Pageable pageable);

    List<Chat> findAllByChatroomCodeAndChatCodeIsLessThan(Long chatroomCode, Long chatCode, Pageable pageable);

    Page<Chat> findAllByChatroomCodeAndChatContentLike(Long chatroomCode, String searchValue, Pageable pageable);
}
