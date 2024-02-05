package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Messenger_ChatRepository")
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByChatroomCodeInAndChatroomMember_Employee_EmployeeCode(List<Long> chatroomCodeList, Long userEmployeeCode);

    @Query(value =
            "SELECT " +
                    "tc.chat_code," +
                    "tcrs.chat_code " +
                    "FROM tbl_chatroom tcr " +
                    "LEFT JOIN tbl_chat tc ON tcr.chatroom_code = tc.chatroom_code " +
                    "LEFT JOIN tbl_chatroom_member tcrm ON tcr.chatroom_code = tcrm.chatroom_code " +
                    "LEFT JOIN tbl_chat_read_status tcrs ON tcrm.chatroom_member_code = tcrs.chatroom_member_code " +
                    "LEFT JOIN tbl_employee te ON tcrm.employee_code = te.employee_code " +
                    "WHERE te.employee_code = :userEmployeeCode " +
                    "AND tc.chat_code = (SELECT MAX(chat_code) FROM tbl_chat tc2 WHERE tc2.chat_code = tc.chat_code)" +
                    "AND tc.chatroom_code = :chatroomCode " +
                    "AND tcrs.chatroom_code = :chatroomCode"
            , nativeQuery = true)
    Long[] findChatCodesInChatAndChatReadStatus(Long chatroomCode, Long userEmployeeCode);
}
