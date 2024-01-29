package com.wittypuppy.backend.calendar.service;

import com.wittypuppy.backend.calendar.dto.CalendarDTO;
import com.wittypuppy.backend.calendar.dto.EmployeeDTO;
import com.wittypuppy.backend.calendar.dto.EventDTO;
import com.wittypuppy.backend.calendar.entity.Calendar;
import com.wittypuppy.backend.calendar.entity.Employee;
import com.wittypuppy.backend.calendar.entity.Event;
import com.wittypuppy.backend.calendar.entity.EventOptions;
import com.wittypuppy.backend.calendar.exception.CreateEventException;
import com.wittypuppy.backend.calendar.exception.DeleteEventException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        List<Employee> employeeList = employeeRepository.findAllByEmployeeRetirementDateBefore(LocalDateTime.now())
                .orElseThrow(() -> new DataNotFoundException("현재 사원 목록 정보가 없습니다."));

        List<EmployeeDTO> employeeDTOList = employeeList.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class)).collect(Collectors.toList());

        log.info("[CalendarService] >>> selectEmployeeList >>> end");
        return employeeDTOList;
    }

    @Transactional
    public String createEvent(EventDTO eventDTO, Long employeeCode) {
        log.info("[CalendarService] >>> createEvent >>> start");
        int result = 0;
        try {
            Calendar calendar = calendarRepository.findByEmployee_EmployeeCodeAndEventList_EventOptions_EventDeleteStatus(employeeCode, "N");
            if (calendar == null) {
                throw new DataNotFoundException("계정의 캘린더를 찾을 수 없습니다.");
            }
            List<Event> eventList = calendar.getEventList();
            if (eventList == null) {
                eventList = new ArrayList<>();
                calendar.setEventList(eventList);
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
            result = 1;
        } catch (Exception e) {
            throw new DataUpdateException("일정 수정에 실패하였습니다.");
        }
        log.info("[CalendarService] >>> modifyEvent >>> end");
        return result > 0 ? "일정 수정 성공" : "일정 수정 실패";
    }

    @Transactional
    public String deleteEvent(Long eventCode, Long employeeCode) {
        log.info("[CalendarService] >>> deleteEvent >>> start");
        /*일정이 N에서 T로 되는가*/
        /*일정이 T에서 완전한 삭제로 되는가*/
        int result = 0;
        try {
            Event event = eventRepository.findById(eventCode).orElseThrow(() -> new DataNotFoundException("해당 일정을 찾을 수 없습니다."));
            EventOptions eventOptions = event.getEventOptions();

            if (eventOptions.getEventDeleteStatus().equals("N")) {
                eventOptions.setEventDeleteStatus("T");
                eventOptions.setEventDeleteTime(LocalDateTime.now());
                result = 1;
            } else if (eventOptions.getEventDeleteStatus().equals("T")) {
                eventRepository.delete(event);
                result = 2;
            } else {
                throw new DeleteEventException("허가되지 않은 일정정보 입니다.");
            }
        } catch (Exception e) {
            throw new DataUpdateException("일정 삭제에 실패하였습니다.");
        }
        log.info("[CalendarService] >>> deleteEvent >>> end");
        return result == 0 ? "일정 삭제 실패" :
                result == 1 ? "일정 임시 삭제 성공" :
                        "일정 완전 삭제 성공";
    }

    public List<EventDTO> selectTemporarilyDeleteEventList(Long employeeCode) {
        log.info("[CalendarService] >>> selectTemporarilyDeleteEventList >>> start");

        Calendar calendar = calendarRepository.findByEmployee_EmployeeCodeAndEventList_EventOptions_EventDeleteStatus(employeeCode, "T");
        if (calendar == null) {
            throw new DataNotFoundException("계정의 캘린더를 찾을 수 없습니다.");
        }
        List<Event> eventList = calendar.getEventList();
        List<EventDTO> eventDTOList = eventList.stream().map(event -> modelMapper.map(event, EventDTO.class)).collect(Collectors.toList());

        log.info("[CalendarService] >>> selectTemporarilyDeleteEventList >>> end");
        return eventDTOList;
    }

    @Transactional
    public String rollbackEvent(Long eventCode, Long employeeCode) {
        log.info("[CalendarService] >>> modifyTemporarilyDeleteEvent >>> start");
        int result = 0;
        try {
            Event event = eventRepository.findById(eventCode).orElseThrow(() -> new DataNotFoundException("해당 일정을 찾을 수 없습니다."));
            EventOptions eventOptions = event.getEventOptions();

            eventOptions.setEventDeleteStatus("N");
            eventOptions.setEventDeleteTime(null);

            result = 1;
        } catch (Exception e) {
            throw new DataUpdateException("일정 롤백에 실패하였습니다.");
        }
        log.info("[CalendarService] >>> modifyTemporarilyDeleteEvent >>> end");
        return result > 0 ? "일정 롤백 성공" : "일정 롤백 실패";
    }
}
