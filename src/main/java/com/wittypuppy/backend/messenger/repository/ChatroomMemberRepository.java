package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.ChatroomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatroomMemberRepository extends JpaRepository<ChatroomMember, Long> {
    Optional<ChatroomMember> findByChatroomCodeAndEmployee_EmployeeCode(Long chatroomCode, Long employeeCode);

    List<ChatroomMember> findAllByChatroomCode(Long chatroomCode);

    Optional<ChatroomMember> findByChatroomMemberCodeAndChatroomCode(Long chatroomMemberCode, Long chatroomCode);
}
