package com.wittypuppy.backend.calendar.repository;

import com.wittypuppy.backend.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("Calendar_CalendarRepository")
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    Optional<Calendar> findByEmployee_EmployeeCode(Long userEmployeeCode);

}
