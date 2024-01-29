package com.wittypuppy.backend.calendar.repository;

import com.wittypuppy.backend.calendar.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Calendar_EventRepository")
public interface EventRepository extends JpaRepository<Event,Long> {
}
