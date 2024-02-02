package com.wittypuppy.backend.messenger.repository;

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
}
