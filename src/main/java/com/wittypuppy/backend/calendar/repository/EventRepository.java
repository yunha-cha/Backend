package com.wittypuppy.backend.calendar.repository;

import com.wittypuppy.backend.calendar.dto.EventDTO;
import com.wittypuppy.backend.calendar.dto.EventInterface;
import com.wittypuppy.backend.calendar.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("Calendar_EventRepository")
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value =
            "SELECT " +
                    "tc.calendar_code calendarCode," +
                    "te.event_code eventCode," +
                    "td.department_name departmentName," +
                    "(SELECT COUNT(*) FROM tbl_event_attendee WHERE event_code = te.event_code) eventAttendeeCount," +
                    "teo.event_title eventTitle," +
                    "teo.event_content eventContent," +
                    "teo.event_isallday eventIsAllDay," +
                    "teo.event_start_date eventStartDate," +
                    "teo.event_end_date eventEndDate," +
                    "teo.event_location eventLocation," +
                    "teo.event_recurrence_rule eventRecurrenceRule," +
                    "teo.event_delete_time eventDeleteTime," +
                    "teo.event_delete_status eventDeleteStatus," +
                    "teo.event_editable eventEditable," +
                    "teo.event_color eventColor," +
                    "teo.event_background_color eventBackgroundColor," +
                    "teo.event_drag_background_color eventDragBackgroundColor," +
                    "teo.event_border_color eventBorderColor," +
                    "teo.event_category eventCategory " +
                    "FROM tbl_calendar tc " +
                    "LEFT JOIN tbl_event te ON tc.calendar_code = te.calendar_code " +
                    "LEFT JOIN tbl_department td ON te.department_code = td.department_code " +
                    "LEFT JOIN tbl_event_options teo ON te.event_code = teo.event_code " +
                    "WHERE tc.calendar_code = :calendarCode "
            , nativeQuery = true)
    List<EventInterface> findAllEventByCalendarCode(Long calendarCode);

    @Query(value =
            "SELECT " +
                    "tc.calendar_code calendarCode," +
                    "te.event_code eventCode," +
                    "td.department_name departmentName," +
                    "(SELECT COUNT(*) FROM tbl_event_attendee WHERE event_code = te.event_code) eventAttendeeCount," +
                    "teo.event_title eventTitle," +
                    "teo.event_content eventContent," +
                    "teo.event_isallday eventIsAllDay," +
                    "teo.event_start_date eventStartDate," +
                    "teo.event_end_date eventEndDate," +
                    "teo.event_location eventLocation," +
                    "teo.event_recurrence_rule eventRecurrenceRule," +
                    "teo.event_delete_time eventDeleteTime," +
                    "teo.event_delete_status eventDeleteStatus," +
                    "teo.event_editable eventEditable," +
                    "teo.event_color eventColor," +
                    "teo.event_background_color eventBackgroundColor," +
                    "teo.event_drag_background_color eventDragBackgroundColor," +
                    "teo.event_border_color eventBorderColor," +
                    "teo.event_category eventCategory " +
                    "FROM tbl_calendar tc " +
                    "LEFT JOIN tbl_event te ON tc.calendar_code = te.calendar_code " +
                    "LEFT JOIN tbl_department td ON te.department_code = td.department_code " +
                    "LEFT JOIN tbl_event_options teo ON te.event_code = teo.event_code " +
                    "WHERE tc.calendar_code = :calendarCode " +
                    "AND te.event_code = :eventCode "
            , nativeQuery = true)
    Optional<EventInterface> findEventInterfaceByCalendarCodeAndEventCode(Long calendarCode,Long eventCode);

    @Query(value =
            "SELECT " +
                    "tc.calendar_code calendarCode," +
                    "te.event_code eventCode," +
                    "td.department_name departmentName," +
                    "(SELECT COUNT(*) FROM tbl_event_attendee WHERE event_code = te.event_code) eventAttendeeCount," +
                    "teo.event_title eventTitle," +
                    "teo.event_content eventContent," +
                    "teo.event_isallday eventIsAllDay," +
                    "teo.event_start_date eventStartDate," +
                    "teo.event_end_date eventEndDate," +
                    "teo.event_location eventLocation," +
                    "teo.event_recurrence_rule eventRecurrenceRule," +
                    "teo.event_delete_time eventDeleteTime," +
                    "teo.event_delete_status eventDeleteStatus," +
                    "teo.event_editable eventEditable," +
                    "teo.event_color eventColor," +
                    "teo.event_background_color eventBackgroundColor," +
                    "teo.event_drag_background_color eventDragBackgroundColor," +
                    "teo.event_border_color eventBorderColor," +
                    "teo.event_category eventCategory " +
                    "FROM tbl_calendar tc " +
                    "LEFT JOIN tbl_event te ON tc.calendar_code = te.calendar_code " +
                    "LEFT JOIN tbl_department td ON te.department_code = td.department_code " +
                    "LEFT JOIN tbl_event_options teo ON te.event_code = teo.event_code " +
                    "WHERE tc.calendar_code = :calendarCode " +
                    "AND teo.event_delete_status='T'"
            , nativeQuery = true)
    List<EventDTO> findAllEventByCalendarCodeAndIsDelete(Long calendarCode);

    @Query(value =
            "SELECT " +
                    "tc.calendar_code calendarCode," +
                    "te.event_code eventCode," +
                    "td.department_name departmentName," +
                    "(SELECT COUNT(*) FROM tbl_event_attendee WHERE event_code = te.event_code) eventAttendeeCount," +
                    "teo.event_title eventTitle," +
                    "teo.event_content eventContent," +
                    "teo.event_isallday eventIsAllDay," +
                    "teo.event_start_date eventStartDate," +
                    "teo.event_end_date eventEndDate," +
                    "teo.event_location eventLocation," +
                    "teo.event_recurrence_rule eventRecurrenceRule," +
                    "teo.event_delete_time eventDeleteTime," +
                    "teo.event_delete_status eventDeleteStatus," +
                    "teo.event_editable eventEditable," +
                    "teo.event_color eventColor," +
                    "teo.event_background_color eventBackgroundColor," +
                    "teo.event_drag_background_color eventDragBackgroundColor," +
                    "teo.event_border_color eventBorderColor," +
                    "teo.event_category eventCategory " +
                    "FROM tbl_calendar tc " +
                    "LEFT JOIN tbl_event te ON tc.calendar_code = te.calendar_code " +
                    "LEFT JOIN tbl_department td ON te.department_code = td.department_code " +
                    "LEFT JOIN tbl_event_options teo ON te.event_code = teo.event_code " +
                    "WHERE tc.calendar_code = :calendarCode " +
                    "AND te.event_code = :eventCode " +
                    "AND teo.event_delete_status='N'"
            , nativeQuery = true)
    Optional<EventDTO> findEventByCalendarCodeAndEventCodeAndIsNotDelete(Long calendarCode, Long eventCode);

    @Query(value =
            "SELECT " +
                    "tc.calendar_code," +
                    "te.event_code," +
                    "td.department_name," +
                    "(SELECT COUNT(*) FROM tbl_event_attendee WHERE event_code = te.event_code)," +
                    "teo.event_title," +
                    "teo.event_content," +
                    "teo.event_isallday ," +
                    "teo.event_start_date," +
                    "teo.event_end_date," +
                    "teo.event_location," +
                    "teo.event_recurrence_rule," +
                    "teo.event_delete_time," +
                    "teo.event_delete_status," +
                    "teo.event_editable," +
                    "teo.event_color," +
                    "teo.event_background_color," +
                    "teo.event_drag_background_color," +
                    "teo.event_border_color " +
                    "FROM tbl_calendar tc " +
                    "LEFT JOIN tbl_event te ON tc.calendar_code = te.calendar_code " +
                    "LEFT JOIN tbl_department td ON te.department_code = td.department_code " +
                    "LEFT JOIN tbl_event_options teo ON te.event_code = teo.event_code " +
                    "WHERE tc.calendar_code = :calendarCode " +
                    "AND te.event_code = :eventCode " +
                    "AND teo.event_delete_status='T'"
            , nativeQuery = true)
    Optional<EventDTO> findEventByCalendarCodeAndEventCodeAndIsDelete(Long calendarCode, Long eventCode);

    Optional<Event> findByCalendarCodeAndEventCode(Long calendarCode, Long eventCode);

    @Query(value =
            "SELECT " +
                    "tc.calendar_code," +
                    "te.event_code," +
                    "td.department_name," +
                    "(SELECT COUNT(*) FROM tbl_event_attendee WHERE event_code = te.event_code)," +
                    "teo.event_title," +
                    "teo.event_content," +
                    "teo.event_start_date," +
                    "teo.event_end_date," +
                    "teo.event_location," +
                    "teo.event_recurrence_rule," +
                    "teo.event_delete_time," +
                    "teo.event_delete_status," +
                    "teo.event_editable," +
                    "teo.event_color," +
                    "teo.event_background_color," +
                    "teo.event_drag_background_color," +
                    "teo.event_border_color," +
                    "teo.event_category eventCategory " +
                    "FROM tbl_calendar tc " +
                    "LEFT JOIN tbl_event te ON tc.calendar_code = te.calendar_code " +
                    "LEFT JOIN tbl_department td ON te.department_code = td.department_code " +
                    "LEFT JOIN tbl_event_options teo ON te.event_code = teo.event_code " +
                    "WHERE tc.calendar_code = :calendarCode " +
                    "AND teo.event_delete_status='N' " +
                    "AND teo.event_title = concat('%',:searchValue,'%') " +
                    "LIMIT :startCount, :searchCount"
            , nativeQuery = true)
    List<EventDTO> selectEventBySearchValueWithPagingWithPaging(Long calendarCode, String searchValue, Integer startCount, Integer searchCount);
}
