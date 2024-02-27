package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.dto.chatroomMainElseChatInterface;
import com.wittypuppy.backend.messenger.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Messenger_ChatroomRepository")
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Optional<Chatroom> findByChatroomCodeAndChatroomMemberList_Employee_EmployeeCode(Long chatroomCode, Long employeeCode);

    @Query(value =
            "SELECT " +
                    "cr.chatroom_code chatroomCode," +
                    "cr.chatroom_title chatroomTitle," +
                    "crpf.chatroom_profile_changed_file chatroomProfileFileURL," +
                    "crs.chat_code lastReadChatCode," +
                    "crm.chatroom_member_pinned_status pinnedStatus " +
                    "FROM tbl_chatroom cr " +
                    "LEFT JOIN tbl_chatroom_profile crpf ON cr.chatroom_code = crpf.chatroom_code " +
                    "LEFT JOIN tbl_chatroom_member crm ON cr.chatroom_code = crm.chatroom_code " +
                    "LEFT JOIN tbl_chat_read_status crs ON cr.chatroom_code = crs.chatroom_code AND crm.chatroom_member_code = crs.chatroom_member_code " +
                    "LEFT JOIN tbl_employee e ON crm.employee_code = e.employee_code " +
                    "WHERE e.employee_code = :employeeCode " +
                    "AND crm.chatroom_member_type <> '삭제' " +
                    "AND cr.chatroom_code = :chatroomCode"
            , nativeQuery = true)
    Optional<chatroomMainElseChatInterface> findByChatroomCodeAndEmployeeCode(Long chatroomCode, Long employeeCode);

    @Query("SELECT " +
            "       cr " +
            "FROM MESSENGER_CHATROOM cr " +
            "   JOIN cr.chatroomMemberList crml " +
            "   JOIN crml.employee e " +
            "WHERE e.employeeCode=:userEmployeeCode " +
            "   AND crml.chatroomMemberType != '삭제'")
    List<Chatroom> findAllChatroomByEmployeeCodeAndDeleteStatus(Long userEmployeeCode);
}
