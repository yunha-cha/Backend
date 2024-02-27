package com.wittypuppy.backend.calendar.controller;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.calendar.dto.CalendarDTO;
import com.wittypuppy.backend.calendar.dto.EventDTO;
import com.wittypuppy.backend.calendar.dto.EventOptionsAboutDateDTO;
import com.wittypuppy.backend.calendar.dto.EventOptionsDTO;
import com.wittypuppy.backend.calendar.service.CalendarService;
import com.wittypuppy.backend.common.dto.Criteria;
import com.wittypuppy.backend.common.dto.PageDTO;
import com.wittypuppy.backend.common.dto.PagingResponseDTO;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "캘린더관련 스웨거 연동")
@RestController
@RequestMapping("/api/v1/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    /**
     * 해당 계정의 캘린더 정보를 반환한다. 일정 정보의 경우 따로 반환해야 한다.
     *
     * @param principal 계정 정보
     * @return 200, 성공 텍스트, 캘린더 정보 반환
     */
    @Tag(name = "캘린더 조회", description = "해당 계정(사원)의 캘린더 정보 조회")
    @GetMapping("")
    public ResponseEntity<ResponseDTO> selectCalendar(
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();

        CalendarDTO calendarDTO = calendarService.selectCalendar(userEmployeeCode);

        return res("캘린더 정보 가져오기 성공", calendarDTO);
    }

    @Tag(name = "일정 조회", description = "해당 계정(사원)의 일정 정보 전체 조회")
    @GetMapping("/events")
    public ResponseEntity<ResponseDTO> selectEvents(
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();

        return res("이벤트 리스트 가져오기 성공", calendarService.selectEvents(userEmployeeCode));
    }

    @GetMapping("/events/search")
    public ResponseEntity<ResponseDTO> selectEventBySearchValueWithPaging(
            @RequestParam(name = "search") String searchValue,
            @RequestParam(name = "offset", defaultValue = "1") String offset,
            @AuthenticationPrincipal User principal) {
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
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();

        return res("이벤트 정보 가져오기 성공", calendarService.selectEventByEventCode(eventCode, userEmployeeCode));
    }

    @Tag(name = "사원 조회", description = "해당 회사의 현재 퇴사하지 않은 전체 사원 목록 조회(사원 초대를 위함)")
    @GetMapping("/employees")
    public ResponseEntity<ResponseDTO> selectEmployeeList(
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("이벤트 참여자 초대를 위한 사원 목록 가져오기 성공", calendarService.selectEmployeeList());
    }

    @Tag(name = "일정 생성", description = "없던 일정을 새로 생성")
    @PostMapping("/events")
    public ResponseEntity<ResponseDTO> createEvent(
            @RequestBody EventOptionsDTO eventOptions,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("일정 생성 성공", calendarService.createEvent(eventOptions, userEmployeeCode));
    }

    @Tag(name = "일정 수정", description = "이미 존재하던 일정을 수정")
    @PutMapping("/events/{eventCode}")
    public ResponseEntity<ResponseDTO> modifyEvent(
            @PathVariable String eventCode,
            @RequestBody EventOptionsDTO eventOptions,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("일정 수정 성공", calendarService.modifyEventOptions(Long.parseLong(eventCode), eventOptions, userEmployeeCode));
    }

    @Tag(name = "일정 수정", description = "단순히 시간만 바뀌는 경우")
    @PutMapping("/events/{eventCode}/date")
    public ResponseEntity<ResponseDTO> modifyEventAboutDate(
            @PathVariable String eventCode,
            @RequestBody EventOptionsAboutDateDTO eventOptionsAboutDate,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res(calendarService.modifyEventOptionsAboutDate(Long.parseLong(eventCode), eventOptionsAboutDate, userEmployeeCode));
    }

    @Tag(name = "일정 삭제", description = "해당 일정을 삭제(기존상태에서 임시삭제, 임시삭제상태에서 영구삭제)")
    @DeleteMapping("/events/{eventCode}")
    public ResponseEntity<ResponseDTO> deleteEvent(
            @PathVariable Long eventCode,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("일정 삭제 성공", calendarService.deleteEvent(eventCode, userEmployeeCode));
    }

    @Tag(name = "임시삭제한 일정 가져오기", description = "임시삭제한 일정을 가져온다.")
    @GetMapping("/events/deleted-temporary")
    public ResponseEntity<ResponseDTO> selectTemporarilyDeleteEventList(
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        List<EventDTO> eventDTOList = calendarService.selectTemporarilyDeleteEventList(userEmployeeCode);
        return res("임시 삭제 일정 목록 가져오기 성공", eventDTOList);
    }

    @Tag(name = "일정 복구", description = "임시삭제상태의 일정 복구")
    @PutMapping("/events/{eventCode}/deleted-rollback")
    public ResponseEntity<ResponseDTO> rollbackEvent(
            @PathVariable Long eventCode,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("일정 롤백 성공", calendarService.rollbackEvent(eventCode, userEmployeeCode));
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
