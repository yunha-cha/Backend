package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.ChatroomProfile;
import com.wittypuppy.backend.messenger.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Messenger_DepartmentRepository")
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
