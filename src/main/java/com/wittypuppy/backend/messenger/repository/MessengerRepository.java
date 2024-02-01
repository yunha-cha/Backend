package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.Messenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessengerRepository extends JpaRepository<Messenger, Long> {
    Optional<Messenger> findByEmployee_EmployeeCode(Long employeeCode);
}
