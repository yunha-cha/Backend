package com.wittypuppy.backend.calendar.service;

import com.wittypuppy.backend.calendar.dto.CalendarDTO;
import com.wittypuppy.backend.calendar.dto.EmployeeDTO;
import com.wittypuppy.backend.calendar.dto.EventDTO;
import com.wittypuppy.backend.calendar.entity.Calendar;
import com.wittypuppy.backend.calendar.entity.Employee;
import com.wittypuppy.backend.calendar.entity.Event;
import com.wittypuppy.backend.calendar.exception.CreateEventException;
import com.wittypuppy.backend.calendar.repository.CalendarRepository;
import com.wittypuppy.backend.calendar.repository.EmployeeRepository;
import com.wittypuppy.backend.calendar.repository.EventRepository;
import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.common.exception.DataUpdateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final EmployeeRepository employeeRepository;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public CalendarDTO selectEventList(Long employeeCode) {
        log.info("[CalendarService] >>> selectEventList >>> start");

        Calendar calendar = calendarRepository.findByEmployee_EmployeeCodeAndEventList_EventOptions_EventDeleteStatus(employeeCode, "N");
        if (calendar == null) {
            Employee employee = employeeRepository.findById(employeeCode).orElseThrow(() -> new DataNotFoundException("계정 정보를 찾을 수 없습니다."));
            calendar = new Calendar();
            calendar.setEmployee(employee);
            calendarRepository.save(calendar);
        }
        CalendarDTO calendarDTO = modelMapper.map(calendar, CalendarDTO.class);

        log.info("[CalendarService] >>> selectEventList >>> end");
        return calendarDTO;
    }

    public EventDTO selectEventByEventCode(Long eventCode, Long employeeCode) {
        log.info("[CalendarService] >>> selectEventByEventCode >>> start");

        Event event = eventRepository.findById(eventCode).orElseThrow(() -> new DataNotFoundException("해당 일정을 찾을 수 없습니다."));
        EventDTO eventDTO = modelMapper.map(event, EventDTO.class);

        log.info("[CalendarService] >>> selectEventByEventCode >>> end");
        return eventDTO;
    }

    public List<EmployeeDTO> selectEmployeeList() {
        log.info("[CalendarService] >>> selectEmployeeList >>> start");

//        List<Employee> employeeList =


        log.info("[CalendarService] >>> selectEmployeeList >>> end");
        return null;
    }

    @Transactional
    public String createEvent(EventDTO eventDTO, Long employeeCode) {
        log.info("[CalendarService] >>> createEvent >>> start");
        int result = 0;
        try {
            Calendar calendar = calendarRepository.findByEmployee_EmployeeCodeAndEventList_EventOptions_EventDeleteStatus(employeeCode, "N");
            List<Event> eventList = calendar.getEventList();
            if (eventList == null) {
                eventList = new ArrayList<>();
            }
            Event newEvent = modelMapper.map(eventDTO, Event.class);
            eventList.add(newEvent);
            result = 1;
        } catch (Exception e) {
            throw new CreateEventException("일정 생성에 실패하였습니다.");
        }
        log.info("[CalendarService] >>> createEvent >>> end");
        return result > 0 ? "일정 만들기 성공" : "일정 만들기 실패";
    }

    @Transactional
    public String modifyEvent(EventDTO eventDTO, Long employeeCode) {
        log.info("[CalendarService] >>> modifyEvent >>> start");
        int result = 0;
        try {
            Long eventCode = eventDTO.getEventCode();
            Event event = eventRepository.findById(eventCode).orElseThrow(() -> new DataNotFoundException("해당 일정을 찾을 수 없습니다."));
            Event modifyEvent = modelMapper.map(eventDTO, Event.class);
            event.setDepartment(modifyEvent.getDepartment());
            event.setEventOptions(modifyEvent.getEventOptions());
            event.setEventAttendeeList(modifyEvent.getEventAttendeeList());
        } catch (Exception e) {
            throw new DataUpdateException("일정 수정에 실패하였습니다.");
        }
        log.info("[CalendarService] >>> modifyEvent >>> end");
        return result > 0 ? "일정 수정 성공" : "일정 수정 실패";
    }

}
