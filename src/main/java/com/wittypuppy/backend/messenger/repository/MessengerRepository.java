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

    @Query("SELECT MCM.chatroomCode, COUNT(MCM) FROM MESSENGER_CHATROOM_MEMBER MCM WHERE MCM.chatroomMemberType!='삭제' GROUP BY MCM.chatroomCode")
    List<Object[]> countByChatroomCodeWhereTypeIsNotDelete();

    @Query(value =
            "SELECT cr.chatroom_code, " +
                    "cr.chatroom_title, " +
                    "cr.chatroom_fixed_status, " +
                    "c.chat_content, " +
                    "c.chat_write_date, " +
                    "cp.chatroom_profile_changed_file, " +
                    "(SELECT COUNT(*) FROM tbl_chatroom_member cm WHERE cm.chatroom_code = cr.chatroom_code) AS tcm_count, " +
                    "FROM tbl_messenger m " +
                    "LEFT JOIN tbl_chatroom cr ON m.messenger_code = cr.messanger_code " +
                    "LEFT JOIN tbl_chatroom_member crm ON cr.chatroom_code = crm.chatroom_code " +
                    "LEFT JOIN tbl_chat c ON c.chatroom_code = cr.chatroom_code " +
                    "LEFT JOIN tbl_chatroom_profile cp ON cp.chatroom_code = cr.chatroom_code " +
                    "WHERE m.employee_code = :employeeCode " +
                    "AND c.chat_write_date = (SELECT MAX(chat_write_date) FROM tbl_chat WHERE chatroom_code = cr.chatroom_code) " +
                    "AND cp.chatroom_profile_regist_date = (SELECT MAX(chatroom_profile_regist_date) FROM tbl_chatroom_profile WHERE chatroom_profile_code = cp.chatroom_profile_code) ",
            nativeQuery = true)
    List<Object[]> x(Long employeeCode);
}
