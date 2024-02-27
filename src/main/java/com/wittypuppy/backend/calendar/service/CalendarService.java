package com.wittypuppy.backend.calendar.service;

import com.wittypuppy.backend.calendar.dto.*;
import com.wittypuppy.backend.calendar.entity.Calendar;
import com.wittypuppy.backend.calendar.entity.Employee;
import com.wittypuppy.backend.calendar.entity.Event;
import com.wittypuppy.backend.calendar.entity.EventOptions;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final EmployeeRepository employeeRepository;
    private final EventOptionsRepository eventOptionsRepository;
    private final EventRepository eventRepository;


    private final ModelMapper modelMapper;

    @Transactional
    public CalendarDTO selectCalendar(Long userEmployeeCode) {
        Employee employee;
        Calendar calendar;
        try {
            employee = employeeRepository.findById(userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 사원이 존재하지 않습니다."));
            if (!Objects.isNull(employee.getEmployeeRetirementDate())) {
                throw new DataNotFoundException("해당 사원은 이미 사퇴했습니다.");
            }
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

        return calendarDTO;
    }

    public List<EventInterface> selectEvents(Long userEmployeeCode) {
        Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("해당 사원의 캘린더가 존재하지 않습니다."));

        System.out.println("calendar: " + calendar.getCalendarCode());

        List<EventInterface> eventList = eventRepository.findAllEventByCalendarCode(calendar.getCalendarCode());

        return eventList;
    }

    public EventDTO selectEventByEventCode(Long eventCode, Long userEmployeeCode) {
        Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("해당 사원의 캘린더가 존재하지 않습니다."));

        EventDTO eventDTO = eventRepository.findEventByCalendarCodeAndEventCodeAndIsNotDelete(calendar.getCalendarCode(), eventCode)
                .orElseThrow(() -> new DataNotFoundException("해당 이벤트가 존재하지 않습니다."));

        return eventDTO;
    }

    public List<EmployeeDTO> selectEmployeeList() {

        List<Employee> employeeList = employeeRepository.findAllByEmployeeRetirementDateIsNull();

        List<EmployeeDTO> employeeDTOList = employeeList.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class)).collect(Collectors.toList());

        return employeeDTOList;
    }

    @Transactional
    public EventInterface createEvent(EventOptionsDTO eventOptions, Long userEmployeeCode) {
        try {
            EventDTO eventDTO = eventOptions.getEvent();

            Calendar calendar = calendarRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 사원의 캘린더가 존재하지 않습니다."));
            Employee employee = employeeRepository.findById(userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 사원이 존재하지 않습니다."));
            System.out.println("eventOptions>>>" + eventOptions);
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
                    .setEventCategory(eventDTO.getEventCategory())
                    .builder();
            System.out.println("eventOptionsEntity" + eventOptionsEntity.toString());
            Event event = new Event()
                    .setCalendarCode(calendar.getCalendarCode())
                    .setDepartment(employee.getDepartment())
                    .setEventOptions(eventOptionsEntity)
                    .builder();
            eventRepository.save(event);
            eventOptionsEntity = eventOptionsEntity
                    .setEventCode(event.getEventCode())
                    .setEventDeleteStatus("N")
                    .setEventEditable("Y")
                    .builder();
            eventOptionsRepository.save(eventOptionsEntity);

            EventInterface returnEvent = eventRepository.findEventInterfaceByCalendarCodeAndEventCode(calendar.getCalendarCode(), event.getEventCode())
                    .orElseThrow(() -> new DataNotFoundException("해당 일정이 존재하지 않습니다."));
            return returnEvent;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataInsertionException("이벤트 생성 실패");
        }
    }

    @Transactional
    public EventInterface modifyEventOptions(Long eventCode, EventOptionsDTO eventOptions, Long userEmployeeCode) {
        try {
            EventDTO eventDTO = eventOptions.getEvent();

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
                    .setEventCategory(eventDTO.getEventCategory())
                    .builder();
            eventOptionsRepository.save(eventOptionsEntity);

            EventInterface returnEvent = eventRepository.findEventInterfaceByCalendarCodeAndEventCode(calendar.getCalendarCode(), eventCode)
                    .orElseThrow(() -> new DataNotFoundException("해당 일정이 존재하지 않습니다."));
            return returnEvent;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataUpdateException("일정 수정 실패");
        }
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
