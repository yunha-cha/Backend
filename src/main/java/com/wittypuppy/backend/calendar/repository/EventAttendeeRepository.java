package com.wittypuppy.backend.calendar.repository;

import com.wittypuppy.backend.calendar.entity.EventAttendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("Calendar_EventAttendeeRepository")
public interface EventAttendeeRepository extends JpaRepository<EventAttendee, Long> {
    List<EventAttendee> findAllByEventCode(Long eventCode);
}
