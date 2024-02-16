package com.wittypuppy.backend.calendar.repository;

import com.wittypuppy.backend.calendar.entity.EventAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Calendar_EventAlertRepository")
public interface EventAlertRepository extends JpaRepository<EventAlert, Long> {
}
