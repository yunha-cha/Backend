package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.ChatroomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Messenger_ChatroomMemberRepository")
public interface ChatroomMemberRepository extends JpaRepository<ChatroomMember, Long> {
    Optional<ChatroomMember> findByChatroomCodeAndEmployee_EmployeeCode(Long chatroomCode, Long employeeCode);

    @Query(value = "SELECT COUNT(*) FROM tbl_chatroom_member tcm WHERE tcm.chatroom_code AND tcm.chatroom_member_type!='삭제'"
            , nativeQuery = true
    )
    Long getChatroomMemberCount(Long chatroomCode);

    Optional<ChatroomMember> findByChatroomMemberCodeAndChatroomCode(Long chatroomMember, Long chatroomCode);

    List<ChatroomMember> findAllByChatroomCodeAndChatroomMemberTypeIn(Long chatroomCode, List<String> chatroomMemberTypeList);

    List<ChatroomMember> findAllByEmployee_EmployeeCode(Long userEmployeeCode);
}
