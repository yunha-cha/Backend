package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.dto.ChatroomMessengerMainInterface;
import com.wittypuppy.backend.messenger.entity.Messenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Messenger_MessengerRepository")
public interface MessengerRepository extends JpaRepository<Messenger, Long> {
    Optional<Messenger> findByEmployee_EmployeeCode(Long employeeCode);

    @Query(value =
            "SELECT " +
                    "cr.chatroom_code chatroomCode, " +
                    "cr.chatroom_title chatroomTitle, " +
                    "crm.chatroom_member_pinned_status chatroomFixedStatus, " +
                    "c.chat_content chatroomContent, " +
                    "c.chat_write_date chatroomChatDate, " +
                    "cp.chatroom_profile_changed_file chatroomProfileFileURL, " +
                    "(SELECT COUNT(*) FROM tbl_chatroom_member cm WHERE cm.chatroom_code = cr.chatroom_code AND cm.chatroom_member_type!='삭제') AS chatroomMemberCount, " +
                    "(SELECT COUNT(*) FROM tbl_chat tc WHERE tc.chatroom_code = (SELECT tcrs.chatroom_code FROM tbl_chat_read_status tcrs WHERE tcrs.chatroom_member_code = crm.chatroom_member_code AND tcrs.chatroom_code = cr.chatroom_code)" +
                    "AND tc.chat_code > COALESCE((SELECT tcrs.chat_code FROM tbl_chat_read_status tcrs WHERE tcrs.chatroom_member_code = crm.chatroom_member_code AND tcrs.chatroom_code = cr.chatroom_code), 0)) AS notReadChatCount " +
                    "FROM  tbl_chatroom cr " +
                    "LEFT JOIN tbl_chatroom_member crm ON cr.chatroom_code = crm.chatroom_code " +
                    "LEFT JOIN tbl_chat c ON c.chatroom_code = cr.chatroom_code " +
                    "LEFT JOIN tbl_chatroom_profile cp ON cp.chatroom_code = cr.chatroom_code " +
                    "LEFT JOIN tbl_employee e ON crm.employee_code = e.employee_code " +
                    "WHERE e.employee_code = :employeeCode " +
                    "AND crm.chatroom_member_type <> '삭제' " +
                    "AND (c.chat_write_date = (SELECT MAX(chat_write_date) FROM tbl_chat WHERE chatroom_code=cr.chatroom_code) OR c.chatroom_code IS null) " +
                    "ORDER BY c.chat_code DESC ",
            nativeQuery = true)
    List<ChatroomMessengerMainInterface> getMessengerStatistics(Long employeeCode);

    @Query(value =
            "SELECT " +
                    "cr.chatroom_code chatroomCode, " +
                    "cr.chatroom_title chatroomTitle, " +
                    "crm.chatroom_member_pinned_status chatroomFixedStatus, " +
                    "c.chat_content chatroomContent, " +
                    "c.chat_write_date chatroomChatDate, " +
                    "cp.chatroom_profile_changed_file chatroomProfileFileURL, " +
                    "(SELECT COUNT(*) FROM tbl_chatroom_member cm WHERE cm.chatroom_code = cr.chatroom_code AND cm.chatroom_member_type!='삭제') AS chatroomMemberCount, " +
                    "(SELECT COUNT(*) FROM tbl_chat tc WHERE tc.chatroom_code = (SELECT tcrs.chatroom_code FROM tbl_chat_read_status tcrs WHERE tcrs.chatroom_member_code = crm.chatroom_member_code AND tcrs.chatroom_code = cr.chatroom_code)" +
                    "AND tc.chat_code > COALESCE((SELECT tcrs.chat_code FROM tbl_chat_read_status tcrs WHERE tcrs.chatroom_member_code = crm.chatroom_member_code AND tcrs.chatroom_code = cr.chatroom_code), 0)) AS notReadChatCount " +
                    "FROM  tbl_chatroom cr " +
                    "LEFT JOIN tbl_chatroom_member crm ON cr.chatroom_code = crm.chatroom_code " +
                    "LEFT JOIN tbl_chat c ON c.chatroom_code = cr.chatroom_code " +
                    "LEFT JOIN tbl_chatroom_profile cp ON cp.chatroom_code = cr.chatroom_code " +
                    "LEFT JOIN tbl_employee e ON crm.employee_code = e.employee_code " +
                    "WHERE e.employee_code = :employeeCode " +
                    "AND crm.chatroom_member_type <> '삭제' " +
                    "AND (c.chat_write_date = (SELECT MAX(chat_write_date) FROM tbl_chat WHERE chatroom_code=cr.chatroom_code) OR c.chatroom_code IS null) " +
                    "AND cr.chatroom_code = :chatroomCode ORDER BY c.chat_code DESC limit 1",
            nativeQuery = true)
    Optional<ChatroomMessengerMainInterface> getMessengerStatisticByChatroomCode(Long employeeCode, Long chatroomCode);
}
