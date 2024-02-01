package com.wittypuppy.backend.calendar.controller;

import com.wittypuppy.backend.calendar.dto.CalendarDTO;
import com.wittypuppy.backend.calendar.dto.EmployeeDTO;
import com.wittypuppy.backend.calendar.dto.EventDTO;
import com.wittypuppy.backend.calendar.service.CalendarService;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calendar")
@AllArgsConstructor
@Slf4j
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("/events")
    public ResponseEntity<ResponseDTO> selectEventList() {
        log.info("[CalendarController] >>> selectEventList >>> start");
        Long employeeCode = 1L;

        CalendarDTO calendarDTO = calendarService.selectEventList(employeeCode);

        log.info("[CalendarController] >>> selectEventList >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "이벤트 리스트 조회 성공", calendarDTO));
    }

    /*일정 하나만 볼 수 있어야 된다.*/
    @GetMapping("/events/{eventCode}")
    public ResponseEntity<ResponseDTO> selectEventByEventCode(
            @PathVariable Long eventCode
    ) {
        log.info("[CalendarController] >>> selectEventByEventCode >>> start");
        Long employeeCode = 1L;

        EventDTO eventDTO = calendarService.selectEventByEventCode(eventCode, employeeCode);

        log.info("[CalendarController] >>> selectEventByEventCode >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "이벤트 리스트 조회 성공", eventDTO));
    }

    /*일정 만들기나 일정 수정하기 할때. 현재 일정에 없는 나머지 사원에 대해서 리스트를 읽어와야 된다.*/
    /*members를 가볍게 가져온다음에 react 측면에서 적절히 filter 하는거로.*/
    @GetMapping("/employees")
    public ResponseEntity<ResponseDTO> selectEmployeeList() {
        log.info("[CalendarController] >>> selectMembers >>> start");

        List<EmployeeDTO> employeeDTOList = calendarService.selectEmployeeList();

        log.info("[CalendarController] >>> selectMembers >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "사원 리스트 조회 성공", employeeDTOList));
    }

    @PostMapping("/events")
    public ResponseEntity<ResponseDTO> createEvent(
            @RequestBody EventDTO eventDTO
    ) {
        log.info("[CalendarController] >>> createEvent >>> start");
        Long employeeCode = 1L;

        String resultStr = calendarService.createEvent(eventDTO, employeeCode);

        log.info("[CalendarController] >>> createEvent >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "이벤트 생성 성공", resultStr));
    }

    @PutMapping("/events/{eventCode}")
    public ResponseEntity<ResponseDTO> modifyEvent(
            @RequestBody EventDTO eventDTO
    ) {
        log.info("[CalendarController] >>> modifyEvent >>> start");
        Long employeeCode = 1L;

        String resultStr = calendarService.modifyEvent(eventDTO, employeeCode);

        log.info("[CalendarController] >>> createEvent >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "이벤트 수정 성공", resultStr));
    }

    /*캘린더 휴지통에 넣는 작업*/
    /*휴지통 일정 삭제하는 작업.*/
    @DeleteMapping("/events/{eventCode}")
    public ResponseEntity<ResponseDTO> deleteEvent(
            @PathVariable Long eventCode
    ) {
        log.info("[CalendarController] >>> deleteEvent >>> start");
        Long employeeCode = 1L;

        String resultStr = calendarService.deleteEvent(eventCode, employeeCode);

        log.info("[CalendarController] >>> deleteEvent >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "이벤트 삭제 작업 성공", resultStr));
    }

    /*휴지통 여는 작업*/
    /*나중에 event -> sql의 event를 이용해서 완전 삭제 되도록.*/
    @GetMapping("/events/{eventCode}/temporarily-delete")
    public ResponseEntity<ResponseDTO> selectTemporarilyDeleteEventList(
            @PathVariable Long eventCode
    ) {
        log.info("[CalendarController] >>> selectTemporarilyDeleteEventList >>> start");
        Long employeeCode = 1L;

        List<EventDTO> eventDTOList = calendarService.selectTemporarilyDeleteEventList(employeeCode);

        log.info("[CalendarController] >>> selectTemporarilyDeleteEventList >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "이벤트 임시 삭제 목록 가져오기 성공", eventDTOList));
    }

    /*휴지통 일정 복구 하는 작업*/
    @PutMapping("/events/{eventCode}/rollback")
    public ResponseEntity<ResponseDTO> rollbackEvent(
            @PathVariable Long eventCode
    ) {
        log.info("[CalendarController] >>> rollbackEvent >>> start");
        Long employeeCode = 1L;

        String resultStr = calendarService.rollbackEvent(eventCode, employeeCode);

        log.info("[CalendarController] >>> rollbackEvent >>> end");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "이벤트 임시 삭제 롤백 성공.", resultStr));
    }
}
