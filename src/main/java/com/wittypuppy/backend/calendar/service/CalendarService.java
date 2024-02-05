package com.wittypuppy.backend.calendar.service;

import com.wittypuppy.backend.calendar.dto.*;
import com.wittypuppy.backend.calendar.entity.*;
import com.wittypuppy.backend.calendar.exception.RollbackEventException;
import com.wittypuppy.backend.calendar.repository.*;
import com.wittypuppy.backend.common.dto.Criteria;
import com.wittypuppy.backend.common.exception.DataInsertionException;
import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.common.exception.DataUpdateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final EventAlertRepository eventAlertRepository;
    private final EventAttendeeRepository eventAttendeeRepository;
    private final EventOptionsRepository eventOptionsRepository;
    private final EventRepository eventRepository;
    private final JobRepository jobRepository;
    private final ProfileRepository profileRepository;


    private final ModelMapper modelMapper;

    @Transactional
    public CalendarDTO selectCalendar(Long userEmployeeCode) {
        log.info("[CalendarService] >>> selectEventList >>> start");
        Employee employee;
        Calendar calendar;
        try {
            employee = employeeRepository.findById(userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 사원이 존재하지 않습니다."));
            if (employee.getEmployeeRetirementDate() != null) {
                throw new DataNotFoundException("해당 사원은 이미 사퇴했습니다.");
            }
            calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                    .orElseGet(() -> {
                        Calendar newCalendar = new Calendar()
                                .setEmployee(employee)
                                .setCalendarName("cal_employee_" + userEmployeeCode)
                                .builder();
                        return newCalendar;
                    });
        } catch (Exception e) {
            throw new DataInsertionException("해당 사원의 캘린더가 존재하지 않아서 생성하려 했으나 실패했습니다.");
        }

        log.info("[CalendarService] >>> selectEventList >>> end");
        CalendarDTO calendarDTO = modelMapper.map(calendar, CalendarDTO.class);

        Optional<Profile> maxDateProfile = calendar.getEmployee().getProfileList().stream().max(Comparator.comparing(Profile::getProfileRegistDate));
        maxDateProfile.ifPresent(profile -> calendarDTO.getEmployee().setProfileImageURL(profile.getProfileChangedFile()));

        return calendarDTO;
    }

    public List<EventDTO> selectEvents(Long userEmployeeCode) {
        Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("해당 사원의 캘린더가 존재하지 않습니다."));

        List<EventDTO> eventList = eventRepository.findAllEventByCalendarCodeAndIsNotDelete(calendar.getCalendarCode());

        return eventList;
    }

    public EventDTO selectEventByEventCode(Long eventCode, Long userEmployeeCode) {
        Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("해당 사원의 캘린더가 존재하지 않습니다."));

        EventDTO event = eventRepository.findEventByCalendarCodeAndEventCodeAndIsNotDelete(calendar.getCalendarCode(), eventCode)
                .orElseThrow(() -> new DataNotFoundException("해당 이벤트가 존재하지 않습니다."));

        return event;
    }

    public List<EventAttendeeDTO> selectEventAttendeeAndEventAlertByEventCode(Long eventCode, Long userEmployeeCode) {
        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAllByEventCode(eventCode);

        List<EventAttendeeDTO> eventAttendeeDTOList = eventAttendeeList.stream().map(eventAttendee -> modelMapper.map(eventAttendee, EventAttendeeDTO.class))
                .collect(Collectors.toList());

        return eventAttendeeDTOList;
    }

    public List<EmployeeDTO> selectEmployeeList() {
        log.info("[CalendarService] >>> selectEmployeeList >>> start");

        List<Employee> employeeList = employeeRepository.findAllByEmployeeRetirementDateIsNull();

        List<EmployeeDTO> employeeDTOList = employeeList.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class)).collect(Collectors.toList());

        log.info("[CalendarService] >>> selectEmployeeList >>> end");
        return employeeDTOList;
    }

    @Transactional
    public String createEvent(EventDTO eventDTO, List<Long> eventAttendeeEmployeeCodeList, EventAlertDTO eventAlertDTO, Long userEmployeeCode) {
        try {
            Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 사원의 캘린더가 존재하지 않습니다."));
            Employee employee = employeeRepository.findById(userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 사원이 존재하지 않습니다."));
            EventOptions eventOptions = new EventOptions()
                    .setEventTitle(eventDTO.getEventTitle())
                    .setEventContent(eventDTO.getEventContent())
                    .setEventStartDate(eventDTO.getEventStartDate())
                    .setEventEndDate(eventDTO.getEventEndDate())
                    .setEventLocation(eventDTO.getEventLocation())
                    .setEventRecurrenceRule(eventDTO.getEventRecurrenceRule())
                    .setEventDeleteTime(eventDTO.getEventDeleteTime())
                    .setEventDeleteStatus(eventDTO.getEventDeleteStatus())
                    .setEventEditable(eventDTO.getEventEditable())
                    .setEventColor(eventDTO.getEventColor())
                    .setEventBackgroundColor(eventDTO.getEventBackgroundColor())
                    .setEventDragBackgroundColor(eventDTO.getEventDragBackgroundColor())
                    .setEventBorderColor(eventDTO.getEventBorderColor())
                    .builder();

            List<Long> newAttendeeEmployeeCodeList = Stream.concat(eventAttendeeEmployeeCodeList.stream(), Stream.of(userEmployeeCode))
                    .distinct()
                    .collect(Collectors.toList());
            List<Employee> newAttendeeEmployeeList = employeeRepository.findAllById(newAttendeeEmployeeCodeList);
            List<EventAttendee> newAttendeeList = newAttendeeEmployeeList.stream().map(newAttendeeEmployee -> {
                EventAttendee eventAttendee = new EventAttendee(null, null, newAttendeeEmployee,
                        new EventAlert(null, null, eventAlertDTO.getEventAlertNotificationTime(), eventAlertDTO.getEventAlertCheckStatus()));
                return eventAttendee;
            }).collect(Collectors.toList());
            Event event = new Event()
                    .setCalendarCode(calendar.getCalendarCode())
                    .setDepartment(employee.getDepartment())
                    .setEventOptions(eventOptions)
                    .setEventAttendeeList(newAttendeeList)
                    .builder();
            eventRepository.save(event);
        } catch (Exception e) {
            throw new DataInsertionException("이벤트 생성 실패");
        }
        return "이벤트 생성 성공";
    }

    @Transactional
    public String modifyEventOptions(Long eventCode, EventDTO eventDTO, Long userEmployeeCode) {
        try {
            Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 캘린더가 존재하지 않습니다."));
            Event event = eventRepository.findByCalendarCodeAndEventCode(calendar.getCalendarCode(), eventCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 이벤트가 존재하지 않습니다."));
            EventOptions eventOptions = eventOptionsRepository.findByEventCode(event.getEventCode())
                    .orElseThrow(() -> new DataNotFoundException("해당 이벤트의 옵션이 존재하지 않습니다."));
            eventOptions = eventOptions
                    .setEventTitle(eventDTO.getEventTitle())
                    .setEventContent(eventDTO.getEventContent())
                    .setEventStartDate(eventDTO.getEventStartDate())
                    .setEventEndDate(eventDTO.getEventEndDate())
                    .setEventLocation(eventDTO.getEventLocation())
                    .setEventRecurrenceRule(eventDTO.getEventRecurrenceRule())
                    .setEventDeleteTime(eventDTO.getEventDeleteTime())
                    .setEventDeleteStatus(eventDTO.getEventDeleteStatus())
                    .setEventEditable(eventDTO.getEventEditable())
                    .setEventColor(eventDTO.getEventColor())
                    .setEventBackgroundColor(eventDTO.getEventBackgroundColor())
                    .setEventDragBackgroundColor(eventDTO.getEventDragBackgroundColor())
                    .setEventBorderColor(eventDTO.getEventBorderColor())
                    .builder();
            eventOptionsRepository.save(eventOptions);
        } catch (Exception e) {
            throw new DataUpdateException("일정 수정 실패");
        }
        return "일정 수정 성공";
    }

    @Transactional
    public String modifyEventAttendeeAndEventAlert(Long eventCode, List<Long> eventAttendeeEmployeeCodeList, EventAlertDTO eventAlertDTO, Long userEmployeeCode) {
        try {
            List<EventAttendee> oldEventAttendeeList = eventAttendeeRepository.findAllByEventCode(eventCode);
            eventAttendeeRepository.deleteAll(oldEventAttendeeList);

            List<Long> newAttendeeEmployeeCodeList = Stream.concat(eventAttendeeEmployeeCodeList.stream(), Stream.of(userEmployeeCode))
                    .distinct()
                    .collect(Collectors.toList());
            List<Employee> newAttendeeEmployeeList = employeeRepository.findAllById(newAttendeeEmployeeCodeList);
            List<EventAttendee> newAttendeeList = newAttendeeEmployeeList.stream().map(newAttendeeEmployee -> {
                EventAttendee eventAttendee = new EventAttendee(null, eventCode, newAttendeeEmployee,
                        new EventAlert(null, null, eventAlertDTO.getEventAlertNotificationTime(), eventAlertDTO.getEventAlertCheckStatus()));
                return eventAttendee;
            }).collect(Collectors.toList());

            eventAttendeeRepository.saveAll(newAttendeeList);
        } catch (Exception e) {
            throw new DataUpdateException("일정 참석자, 알람 수정 실패");
        }
        return "일정 참석자, 알람 수정 성공";
    }

    @Transactional
    public String deleteEvent(Long eventCode, Long userEmployeeCode) {
        log.info("[CalendarService] >>> deleteEvent >>> start");
        /*일정이 N에서 T로 되는가*/
        /*일정이 T에서 완전한 삭제로 되는가*/
        LocalDateTime now = LocalDateTime.now();
        try {
            Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 캘린더가 존재하지 않습니다."));
            Event event = eventRepository.findByCalendarCodeAndEventCode(calendar.getCalendarCode(), eventCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 이벤트가 존재하지 않습니다."));
            EventOptions eventOptions = event.getEventOptions();

            if (eventOptions.getEventDeleteStatus().equals("N")) {
                eventOptions.setEventDeleteStatus("T");
                eventOptions.setEventDeleteTime(now);
                eventOptionsRepository.save(eventOptions);
                return "일정 임시 삭제 성공";
            } else if (eventOptions.getEventDeleteStatus().equals("T")) {
                eventRepository.delete(event);
                return "일정 완전 삭제 성공";
            }
        } catch (Exception e) {
            throw new DataUpdateException("일정 삭제에 실패하였습니다.");
        }
        return "알 수 없는 오류 발생";
    }

    public List<EventDTO> selectTemporarilyDeleteEventList(Long userEmployeeCode) {
        Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("해당 캘린더가 존재하지 않습니다."));
        List<EventDTO> eventDTOList = eventRepository.findAllEventByCalendarCodeAndIsDelete(calendar.getCalendarCode());
        return eventDTOList;
    }

    @Transactional
    public String rollbackEvent(Long eventCode, Long userEmployeeCode) {
        try {
            Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 캘린더가 존재하지 않습니다."));
            Event event = eventRepository.findByCalendarCodeAndEventCode(calendar.getCalendarCode(), eventCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 일정을 찾을 수 없습니다."));
            EventOptions eventOptions = event.getEventOptions();

            if (eventOptions.getEventDeleteStatus().equals("T")) {
                eventOptions.setEventDeleteStatus("N");
                eventOptions.setEventDeleteTime(null);
            } else {
                throw new RollbackEventException("해당 일정은 임시 삭제 상태가 아닙니다.");
            }
            eventOptionsRepository.save(eventOptions);
        } catch (Exception e) {
            throw new DataUpdateException("일정 롤백 실패");
        }
        return "일정 롤백 성공";
    }

    public List<EventDTO> selectEventBySearchValueWithPaging(Long userEmployeeCode, String searchValue, Criteria cri) {
        Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("해당 캘린더가 존재하지 않습니다."));

        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        List<EventDTO> eventList = eventRepository.selectEventBySearchValueWithPagingWithPaging(calendar.getCalendarCode(), searchValue, index * count, count);

        return eventList;
    }
}
