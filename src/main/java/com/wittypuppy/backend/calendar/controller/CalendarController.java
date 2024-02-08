package com.wittypuppy.backend.calendar.controller;

import com.wittypuppy.backend.Employee.dto.EmployeeDTO;
import com.wittypuppy.backend.calendar.dto.CalendarDTO;
import com.wittypuppy.backend.calendar.dto.EventAlertDTO;
import com.wittypuppy.backend.calendar.dto.EventAttendeeDTO;
import com.wittypuppy.backend.calendar.dto.EventDTO;
import com.wittypuppy.backend.calendar.service.CalendarService;
import com.wittypuppy.backend.common.dto.Criteria;
import com.wittypuppy.backend.common.dto.PageDTO;
import com.wittypuppy.backend.common.dto.PagingResponseDTO;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/calendar")
@AllArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("")
    public ResponseEntity<ResponseDTO> selectCalendar(
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();

        CalendarDTO calendarDTO = calendarService.selectCalendar(userEmployeeCode);

        return res("캘린더 정보 가져오기 성공", calendarDTO);
    }

    @GetMapping("/events")
    public ResponseEntity<ResponseDTO> selectEvents(
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();

        List<EventDTO> eventDTOList = calendarService.selectEvents(userEmployeeCode);

        return res("이벤트 리스트 가져오기 성공", eventDTOList);
    }

    @GetMapping("/events/search")
    public ResponseEntity<ResponseDTO> selectEventBySearchValueWithPaging(
            @RequestParam(name = "search") String searchValue,
            @RequestParam(name = "offset", defaultValue = "1") String offset,
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        Criteria cri = new Criteria(Integer.valueOf(offset), 10);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        List<EventDTO> eventDTOList = calendarService.selectEventBySearchValueWithPaging(userEmployeeCode, searchValue, cri);
        pagingResponseDTO.setData(eventDTOList);
        pagingResponseDTO.setPageInfo(new PageDTO(cri, (int) eventDTOList.size()));

        return res("이벤트 검색 결과 가져오기 성공", pagingResponseDTO);
    }

    @GetMapping("/events/{eventCode}")
    public ResponseEntity<ResponseDTO> selectEventInfoByEventCode(
            @PathVariable Long eventCode,
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();

        Map<String, Object> resultMap = calendarService.selectEventByEventCode(eventCode, userEmployeeCode);

        return res("이벤트 정보 가져오기 성공", resultMap);
    }

    @GetMapping("/employees")
    public ResponseEntity<ResponseDTO> selectEmployeeList(
            @AuthenticationPrincipal Object object) {
        return res("이벤트 참여자 초대를 위한 사원 목록 가져오기 성공", calendarService.selectEmployeeList());
    }

    @PostMapping("/events")
    public ResponseEntity<ResponseDTO> createEvent(
            @RequestBody EventDTO eventDTO,
            @RequestBody List<Long> eventAttendeeEmployeeCodeList,
            @RequestBody EventAlertDTO eventAlertDTO,
            @AuthenticationPrincipal Object object) {
        Long userEmployeeCode = 12L;
        return res("이벤트 만들기 성공", calendarService.createEvent(eventDTO, eventAttendeeEmployeeCodeList, eventAlertDTO, userEmployeeCode));
    }

    @PutMapping("/events/{eventCode}/options")
    public ResponseEntity<ResponseDTO> modifyEventOptions(
            @PathVariable Long eventCode,
            @RequestBody EventDTO eventDTO,
            @AuthenticationPrincipal Object object) {
        Long userEmployeeCode = 12L;
        return res(calendarService.modifyEventOptions(eventCode, eventDTO, userEmployeeCode));
    }

    @PutMapping("/events/{eventCode}/attendee-and-alert")
    public ResponseEntity<ResponseDTO> modifyEventAttendeeAndEventAlert(
            @PathVariable Long eventCode,
            @RequestBody List<Long> eventAttendeeEmployeeCodeList,
            @RequestBody EventAlertDTO eventAlertDTO,
            @AuthenticationPrincipal Object object) {
        Long userEmployeeCode = 12L;
        return res(calendarService.modifyEventAttendeeAndEventAlert(eventCode, eventAttendeeEmployeeCodeList, eventAlertDTO, userEmployeeCode));
    }

    @DeleteMapping("/events/{eventCode}")
    public ResponseEntity<ResponseDTO> deleteEvent(
            @PathVariable Long eventCode,
            @AuthenticationPrincipal Object object) {
        Long userEmployeeCode = 12L;
        return res(calendarService.deleteEvent(eventCode, userEmployeeCode));
    }

    @GetMapping("/events/deleted-temporary")
    public ResponseEntity<ResponseDTO> selectTemporarilyDeleteEventList(
            @AuthenticationPrincipal Object object) {
        Long userEmployeeCode = 12L;
        List<EventDTO> eventDTOList = calendarService.selectTemporarilyDeleteEventList(userEmployeeCode);
        return res("임시 삭제 일정 목록 가져오기 성공", eventDTOList);
    }

    @PutMapping("/events/{eventCode}/deleted-rollback")
    public ResponseEntity<ResponseDTO> rollbackEvent(
            @PathVariable Long eventCode,
            @AuthenticationPrincipal Object object) {
        Long userEmployeeCode = 12L;
        return res(calendarService.rollbackEvent(eventCode, userEmployeeCode));
    }


    /**
     * 정상적인 조회에 성공했을 경우 응답하는 메서드
     *
     * @param msg  메시지
     * @param data 보낼 데이터
     * @return 200, 메시지, 보낼데이터 로 응답
     */
    private ResponseEntity<ResponseDTO> res(String msg, Object data) {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, msg, data));
    }

    /**
     * 정상적인 생성, 수정, 삭제에 성공할 경우 응답하는 메서드<br>
     * 반환할 데이터가 없고 이미 메시지에 어느정도 설명이 있으므로 이 Object를 생략한 값을 반환한다.
     *
     * @param msg 메시지
     * @return 200, 메시지 로 응답
     */
    private ResponseEntity<ResponseDTO> res(String msg) {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, msg));
    }
}
