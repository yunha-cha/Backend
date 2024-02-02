package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Messenger_ChatroomRepository")
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Optional<Chatroom> findByChatroomCodeAndChatroomMemberList_Employee_EmployeeCode(Long chatroomCode, Long employeeCode);
}
