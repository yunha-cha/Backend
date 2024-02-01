package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.Chatroom;
import com.wittypuppy.backend.messenger.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
}
