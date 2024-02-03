package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.dto.ChatroomMessengerMainDTO;
import com.wittypuppy.backend.messenger.entity.Messenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Messenger_MessengerRepository")
public interface MessengerRepository extends JpaRepository<Messenger, Long> {
    Optional<Messenger> findByEmployee_EmployeeCode(Long employeeCode);

    Optional<Messenger> findByEmployee_EmployeeCodeAndChatroomList_ChatroomCode(Long employeeCode, Long chatroomCode);

    @Query(value =
            "SELECT cr.chatroom_code chatroomCode, " +
                    "cr.chatroom_title chatroomTitle, " +
                    "crm.chatroom_member_pinned_status chatroomFixedStatus, " +
                    "c.chat_content chatroomContent, " +
                    "c.chat_write_date chatrootChatDate, " +
                    "cp.chatroom_profile_changed_file chatroomProfileFileURL, " +
                    "(SELECT COUNT(*) FROM tbl_chatroom_member cm WHERE cm.chatroom_code = cr.chatroom_code AND cm.chatroom_member_type!='삭제') AS chatroomMemberCount, " +
                    "(SELECT COUNT(*) FROM tbl_chat tc WHERE tc.chatroom_code = (SELECT tcrs.chatroom_code FROM tbl_chat_read_status tcrs WHERE tcrs.chatroom_member_code = crm.chatroom_member_code AND tcrs.chatroom_code = cr.chatroom_code)" +
                    "AND tc.chat_code > (SELECT tcrs.chat_code FROM tbl_chat_read_status tcrs WHERE tcrs.chatroom_member_code = crm.chatroom_member_code AND tcrs.chatroom_code = cr.chatroom_code)) AS notReadChatCount " +
                    "FROM tbl_messenger m " +
                    "LEFT JOIN tbl_chatroom cr ON m.messenger_code = cr.messanger_code " +
                    "LEFT JOIN tbl_chatroom_member crm ON cr.chatroom_code = crm.chatroom_code " +
                    "LEFT JOIN tbl_chat c ON c.chatroom_code = cr.chatroom_code " +
                    "LEFT JOIN tbl_chatroom_profile cp ON cp.chatroom_code = cr.chatroom_code " +
                    "WHERE m.employee_code = :employeeCode " +
                    "AND c.chat_write_date = (SELECT MAX(chat_write_date) FROM tbl_chat WHERE chatroom_code = cr.chatroom_code) " +
                    "AND cp.chatroom_profile_regist_date = (SELECT MAX(chatroom_profile_regist_date) FROM tbl_chatroom_profile WHERE chatroom_profile_code = cp.chatroom_profile_code) ",
            nativeQuery = true)
    List<ChatroomMessengerMainDTO> getMessengerStatistics(Long employeeCode);
}
