package com.wittypuppy.backend.calendar.service;

import com.wittypuppy.backend.calendar.dto.*;
import com.wittypuppy.backend.calendar.entity.Calendar;
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
import java.time.ZoneId;
import java.util.*;
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
            log.info("employee1 : " + employee.toString());
            if (!Objects.isNull(employee.getEmployeeRetirementDate())) {
                throw new DataNotFoundException("해당 사원은 이미 사퇴했습니다.");
            }
            log.info("employee2 : " + employee.toString());
            calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                    .orElseGet(() -> {
                        Calendar newCalendar = new Calendar()
                                .setEmployee(employee)
                                .setCalendarName("cal_employee_" + userEmployeeCode)
                                .builder();
                        calendarRepository.save(newCalendar);
                        return newCalendar;
                    });
        } catch (Exception e) {
            throw new DataInsertionException("해당 사원의 캘린더가 존재하지 않아서 생성하려 했으나 실패했습니다.");
        }

        CalendarDTO calendarDTO = modelMapper.map(calendar, CalendarDTO.class);
        log.info("[CalendarService] >>> selectEventList >>> end");

        return calendarDTO;
    }

    public List<EventInterfaceAndEventAttendeesDTO> selectEvents(Long userEmployeeCode) {
        Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("해당 사원의 캘린더가 존재하지 않습니다."));

        System.out.println("calendar: " + calendar.getCalendarCode());

        List<EventInterface> eventList = eventRepository.findAllEventByCalendarCode(calendar.getCalendarCode());
        log.info("eventList>>>" + eventList.toString());
        List<EventInterfaceAndEventAttendeesDTO> eventInterfaceAndEventAttendeesDTO = eventList.stream().map(
                eventDTO -> {
                    List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAllByEventCode(eventDTO.getEventCode());
                    List<EventAttendeeDTO> eventAttendeeDTOList = eventAttendeeList.stream().map(eventAttendee -> modelMapper.map(eventAttendee, EventAttendeeDTO.class))
                            .collect(Collectors.toList());
                    EventInterfaceAndEventAttendeesDTO dto = new EventInterfaceAndEventAttendeesDTO(eventDTO, eventAttendeeDTOList);
                    return dto;
                }
        ).collect(Collectors.toList());

        return eventInterfaceAndEventAttendeesDTO;
    }

    public Map<String, Object> selectEventByEventCode(Long eventCode, Long userEmployeeCode) {
        Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("해당 사원의 캘린더가 존재하지 않습니다."));

        Map<String, Object> eventInfo = new HashMap<>();

        EventDTO event = eventRepository.findEventByCalendarCodeAndEventCodeAndIsNotDelete(calendar.getCalendarCode(), eventCode)
                .orElseThrow(() -> new DataNotFoundException("해당 이벤트가 존재하지 않습니다."));

        eventInfo.put("Event", event);

        List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAllByEventCode(eventCode);
        List<EventAttendeeDTO> eventAttendeeDTOList = eventAttendeeList.stream().map(eventAttendee -> modelMapper.map(eventAttendee, EventAttendeeDTO.class))
                .collect(Collectors.toList());

        eventInfo.put("EventAttendeeList", eventAttendeeDTOList);

        return eventInfo;
    }

    public List<EmployeeDTO> selectEmployeeList() {
        log.info("[CalendarService] >>> selectEmployeeList >>> start");

        List<Employee> employeeList = employeeRepository.findAllByEmployeeRetirementDateIsNull();

        List<EmployeeDTO> employeeDTOList = employeeList.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class)).collect(Collectors.toList());

        log.info("[CalendarService] >>> selectEmployeeList >>> end");
        return employeeDTOList;
    }

    @Transactional
    public EventInterfaceAndEventAttendeesDTO createEvent(EventOptionsDTO eventOptions, Long userEmployeeCode) {
        EventInterfaceAndEventAttendeesDTO returnDTO;
        try {
            EventDTO eventDTO = eventOptions.getEvent();
            EventAlertDTO eventAlertDTO = eventOptions.getEventAlert();
            List<Long> eventAttendeeEmployeeCodeList = eventOptions.getEventAttendeeEmployeeCodeList();

            Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 사원의 캘린더가 존재하지 않습니다."));
            Employee employee = employeeRepository.findById(userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 사원이 존재하지 않습니다."));
            EventOptions eventOptionsEntity = new EventOptions()
                    .setEventCode(null)
                    .setEventDeleteStatus("N")
                    .setEventEditable("Y")
                    .setEventTitle(eventDTO.getEventTitle())
                    .setEventContent(eventDTO.getEventContent())
                    .setEventStartDate(eventDTO.getEventStartDate())
                    .setEventEndDate(eventDTO.getEventEndDate())
                    .setEventLocation(eventDTO.getEventLocation())
                    .setEventRecurrenceRule(eventDTO.getEventRecurrenceRule())
                    .setEventIsAllDay(eventDTO.getEventIsAllDay())
                    .setEventColor(eventDTO.getEventColor())
                    .setEventBackgroundColor(eventDTO.getEventBackgroundColor())
                    .setEventDragBackgroundColor(eventDTO.getEventDragBackgroundColor())
                    .setEventBorderColor(eventDTO.getEventBorderColor())
                    .builder();
            System.out.println("eventOptionsEntity" + eventOptionsEntity.toString());
            List<Long> newAttendeeEmployeeCodeList = Stream.concat(eventAttendeeEmployeeCodeList.stream(), Stream.of(userEmployeeCode))
                    .distinct()
                    .collect(Collectors.toList());
            List<Employee> newAttendeeEmployeeList = employeeRepository.findAllById(newAttendeeEmployeeCodeList);
            List<EventAttendee> newAttendeeList = newAttendeeEmployeeList.stream().map(newAttendeeEmployee -> {
                EventAttendee eventAttendee = new EventAttendee(null, null, newAttendeeEmployee,
//                        new EventAlert(null, null, eventAlertDTO.getEventAlertNotificationTime(), eventAlertDTO.getEventAlertCheckStatus())
                        null
                );
                return eventAttendee;
            }).collect(Collectors.toList());
            Event event = new Event()
                    .setCalendarCode(calendar.getCalendarCode())
                    .setDepartment(employee.getDepartment())
                    .setEventOptions(eventOptionsEntity)
                    .setEventAttendeeList(newAttendeeList)
                    .builder();
            eventRepository.save(event);
            eventOptionsEntity = eventOptionsEntity
                    .setEventCode(event.getEventCode())
                    .setEventDeleteStatus("N")
                    .setEventEditable("Y")
                    .builder();
            eventOptionsRepository.save(eventOptionsEntity);
            eventAttendeeRepository.saveAll(newAttendeeList);

            EventInterface returnEvent = eventRepository.findEventInterfaceByCalendarCodeAndEventCode(calendar.getCalendarCode(), event.getEventCode())
                    .orElseThrow(() -> new DataNotFoundException("해당 일정이 존재하지 않습니다."));
            List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAllByEventCode(event.getEventCode());
            System.out.println("eventAttendeeList>>>>" + eventAttendeeList.toString());
            List<EventAttendeeDTO> eventAttendeeDTOList = eventAttendeeList.stream().map(eventAttendee -> modelMapper.map(eventAttendee, EventAttendeeDTO.class))
                    .collect(Collectors.toList());
            returnDTO = new EventInterfaceAndEventAttendeesDTO(returnEvent, eventAttendeeDTOList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataInsertionException("이벤트 생성 실패");
        }
        return returnDTO;
    }

    @Transactional
    public EventInterfaceAndEventAttendeesDTO modifyEventOptions(Long eventCode, EventOptionsDTO eventOptions, Long userEmployeeCode) {
        EventInterfaceAndEventAttendeesDTO returnDTO;
        System.out.println(">>>>>>>" + eventOptions.getEvent().toString());
        try {
            EventDTO eventDTO = eventOptions.getEvent();
            EventAlertDTO eventAlertDTO = eventOptions.getEventAlert();
            List<Long> eventAttendeeEmployeeCodeList = eventOptions.getEventAttendeeEmployeeCodeList();

            Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 캘린더가 존재하지 않습니다."));

            Event event = eventRepository.findByCalendarCodeAndEventCode(calendar.getCalendarCode(), eventCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 이벤트가 존재하지 않습니다."));
            EventOptions eventOptionsEntity = eventOptionsRepository.findByEventCode(event.getEventCode())
                    .orElseThrow(() -> new DataNotFoundException("해당 이벤트의 옵션이 존재하지 않습니다."));
            eventOptionsEntity = eventOptionsEntity
                    .setEventTitle(eventDTO.getEventTitle())
                    .setEventContent(eventDTO.getEventContent())
                    .setEventStartDate(eventDTO.getEventStartDate())
                    .setEventEndDate(eventDTO.getEventEndDate())
                    .setEventLocation(eventDTO.getEventLocation())
                    .setEventRecurrenceRule(eventDTO.getEventRecurrenceRule())
                    .setEventIsAllDay(eventDTO.getEventIsAllDay())
                    .setEventColor(eventDTO.getEventColor())
                    .setEventBackgroundColor(eventDTO.getEventBackgroundColor())
                    .setEventDragBackgroundColor(eventDTO.getEventDragBackgroundColor())
                    .setEventBorderColor(eventDTO.getEventBorderColor())
                    .builder();
            eventOptionsRepository.save(eventOptionsEntity);

            List<EventAttendee> oldEventAttendeeList = eventAttendeeRepository.findAllByEventCode(eventCode);
            eventAttendeeRepository.deleteAll(oldEventAttendeeList);

            List<Long> newAttendeeEmployeeCodeList = Stream.concat(eventAttendeeEmployeeCodeList.stream(), Stream.of(userEmployeeCode))
                    .distinct()
                    .collect(Collectors.toList());
            List<Employee> newAttendeeEmployeeList = employeeRepository.findAllById(newAttendeeEmployeeCodeList);
            List<EventAttendee> newAttendeeList = newAttendeeEmployeeList.stream().map(newAttendeeEmployee -> {
                EventAttendee eventAttendee = new EventAttendee(null, eventCode, newAttendeeEmployee,
                        // new EventAlert(null, null, eventAlertDTO.getEventAlertNotificationTime(), eventAlertDTO.getEventAlertCheckStatus())
                        null
                );
                return eventAttendee;
            }).collect(Collectors.toList());
            eventAttendeeRepository.saveAll(newAttendeeList);

            EventInterface returnEvent = eventRepository.findEventInterfaceByCalendarCodeAndEventCode(calendar.getCalendarCode(), eventCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 일정이 존재하지 않습니다."));
            List<EventAttendee> eventAttendeeList = eventAttendeeRepository.findAllByEventCode(eventDTO.getEventCode());
            List<EventAttendeeDTO> eventAttendeeDTOList = eventAttendeeList.stream().map(eventAttendee -> modelMapper.map(eventAttendee, EventAttendeeDTO.class))
                    .collect(Collectors.toList());
            returnDTO = new EventInterfaceAndEventAttendeesDTO(returnEvent, eventAttendeeDTOList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataUpdateException("일정 수정 실패");
        }
        return returnDTO;
    }

    public String modifyEventOptionsAboutDate(long eventCode, EventOptionsAboutDateDTO eventOptionsAboutDate, Long userEmployeeCode) {
        try {
            System.out.println("시간좀 확인" + eventOptionsAboutDate.toString());
            EventOptions eventOptionsEntity = eventOptionsRepository.findByEventCode(eventCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 이벤트의 옵션이 존재하지 않습니다."));
            System.out.println("전달한 값시작" + eventOptionsAboutDate.getStartDate());
            System.out.println("전달한 값끝끝" + eventOptionsAboutDate.getEndDate());
            eventOptionsEntity = eventOptionsEntity
                    .setEventEndDate(eventOptionsAboutDate.getEndDate())
                    .builder();
            if (eventOptionsAboutDate.getStartDate() != null) {
                eventOptionsEntity = eventOptionsEntity.setEventStartDate(eventOptionsAboutDate.getStartDate())
                        .builder();
                if (eventOptionsAboutDate.getIsAllday().equals("Y")) {
                    Date endDate = eventOptionsAboutDate.getEndDate();
                    endDate.setHours(0);
                    endDate.setMinutes(0);
                    endDate.setSeconds(0);
                    System.out.println("endData>>" + endDate);
                    eventOptionsEntity = eventOptionsEntity
                            .setEventEndDate(endDate)
                            .builder();
                }
            }
            System.out.println("실제 저장시작" + eventOptionsEntity.getEventStartDate());
            System.out.println("실제 저장끝끝" + eventOptionsEntity.getEventEndDate());
            eventOptionsRepository.save(eventOptionsEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataUpdateException("일정 시간 수정 실패");
        }
        return "일정 시간 수정 성공";
    }


    @Transactional
    public EventAndCalendarCodeDTO deleteEvent(Long eventCode, Long userEmployeeCode) {
        log.info("[CalendarService] >>> deleteEvent >>> start");
        EventAndCalendarCodeDTO eventAndCalendarCode = new EventAndCalendarCodeDTO();
        /*일정이 N에서 T로 되는가*/
        /*일정이 T에서 완전한 삭제로 되는가*/
        LocalDateTime now = LocalDateTime.now();
        try {
            Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 캘린더가 존재하지 않습니다."));
            Event event = eventRepository.findByCalendarCodeAndEventCode(calendar.getCalendarCode(), eventCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 이벤트가 존재하지 않습니다."));
            EventOptions eventOptions = event.getEventOptions();
            eventAndCalendarCode.setEventCode(event.getEventCode());
            eventAndCalendarCode.setCalendarCode(event.getCalendarCode());
            if (eventOptions.getEventDeleteStatus().equals("N")) {
                eventOptions.setEventDeleteStatus("T");
                eventOptions.setEventDeleteTime(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
                eventOptionsRepository.save(eventOptions);
                eventAndCalendarCode.setState("tempDelete");
                return eventAndCalendarCode;
            } else if (eventOptions.getEventDeleteStatus().equals("T")) {
                eventOptions.setEventDeleteStatus("Y");
                eventOptions.setEventDeleteTime(null);
                eventOptionsRepository.save(eventOptions);
                eventAndCalendarCode.setState("completeDelete");
                return eventAndCalendarCode;
            } else {
                throw new DataUpdateException("일정 삭제에 실패하였습니다.");
            }
        } catch (Exception e) {
            throw new DataUpdateException("일정 삭제에 실패하였습니다.");
        }
    }

    public List<EventDTO> selectTemporarilyDeleteEventList(Long userEmployeeCode) {
        Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("해당 캘린더가 존재하지 않습니다."));
        List<EventDTO> eventDTOList = eventRepository.findAllEventByCalendarCodeAndIsDelete(calendar.getCalendarCode());
        return eventDTOList;
    }

    @Transactional
    public EventAndCalendarCodeDTO rollbackEvent(Long eventCode, Long userEmployeeCode) {
        EventAndCalendarCodeDTO eventAndCalendarCode = new EventAndCalendarCodeDTO();
        try {
            Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 캘린더가 존재하지 않습니다."));
            Event event = eventRepository.findByCalendarCodeAndEventCode(calendar.getCalendarCode(), eventCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 일정을 찾을 수 없습니다."));
            EventOptions eventOptions = event.getEventOptions();

            eventAndCalendarCode.setEventCode(event.getEventCode());
            eventAndCalendarCode.setCalendarCode(calendar.getCalendarCode());

            if (eventOptions.getEventDeleteStatus().equals("T")) {
                eventOptions.setEventDeleteStatus("N");
                eventOptions.setEventDeleteTime(null);
                eventAndCalendarCode.setState("rollback");
            } else {
                throw new RollbackEventException("해당 일정은 임시 삭제 상태가 아닙니다.");
            }
            eventOptionsRepository.save(eventOptions);
            return eventAndCalendarCode;
        } catch (Exception e) {
            throw new DataUpdateException("일정 롤백 실패");
        }
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
