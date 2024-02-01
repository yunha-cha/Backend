package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.Employee;
import com.wittypuppy.backend.messenger.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Messenger_JobRepository")
public interface JobRepository extends JpaRepository<Job, Long> {
}
