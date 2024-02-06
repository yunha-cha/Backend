package com.wittypuppy.backend.calendar.repository;

import com.wittypuppy.backend.calendar.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Calendar_JobRepository")
public interface JobRepository extends JpaRepository<Job, Long> {
}
