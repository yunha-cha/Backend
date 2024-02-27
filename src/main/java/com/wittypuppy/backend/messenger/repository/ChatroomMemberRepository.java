package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.ChatroomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Messenger_ChatroomMemberRepository")
public interface ChatroomMemberRepository extends JpaRepository<ChatroomMember, Long> {
    Optional<ChatroomMember> findByChatroomCodeAndEmployee_EmployeeCodeAndChatroomMemberTypeNot(Long chatroomCode, Long employeeCode, String chatroomMemberType);

    @Query("SELECT CM FROM MESSENGER_CHATROOM_MEMBER CM " +
            "LEFT JOIN CM.employee e " +
            "WHERE CM.chatroomCode = :chatroomCode " +
            "AND e.employeeCode = :employeeCode " +
            "And CM.chatroomMemberType != '삭제'")
    Optional<ChatroomMember> findByChatroomCodeAndEmployeeCodeAndIsNotDelete(Long chatroomCode, Long employeeCode);

    @Query(value = "SELECT COUNT(*) FROM tbl_chatroom_member tcm WHERE tcm.chatroom_code AND tcm.chatroom_member_type!='삭제'"
            , nativeQuery = true
    )

    Optional<ChatroomMember> findByChatroomMemberCodeAndChatroomCode(Long chatroomMember, Long chatroomCode);

    List<ChatroomMember> findAllByChatroomCodeAndChatroomMemberTypeIn(Long chatroomCode, List<String> chatroomMemberTypeList);

    List<ChatroomMember> findAllByChatroomCodeAndChatroomMemberTypeIsNot(Long chatroomCode, String chatroomMemberType);
}
