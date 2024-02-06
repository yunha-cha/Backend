package com.wittypuppy.backend.calendar.repository;

import com.wittypuppy.backend.calendar.entity.EventOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("Calendar_EventOptionsRepository")
public interface EventOptionsRepository extends JpaRepository<EventOptions, Long> {
    Optional<EventOptions> findByEventCode(Long eventCode);
}
