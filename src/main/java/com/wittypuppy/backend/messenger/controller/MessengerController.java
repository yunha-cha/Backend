package com.wittypuppy.backend.messenger.controller;

import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.messenger.service.MessengerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/messenger")
@AllArgsConstructor
@Slf4j
public class MessengerController {
    private final MessengerService messengerService;

    /**
     * front 측면에서 로그인에 성공할 경우 채팅방 목록을 가져온 다음에<br>
     * 이 채팅방 목록의 코드값을 이용해서 handShake를 먼저 진행해준다.<br>
     * 말그대로 내가 속한 채팅방의 식별 코드를 구한다.
     *
     * @param object 계정 정보
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<ResponseDTO> selectChatroomCodeListAndIsAlert(
            @AuthenticationPrincipal Object object) {
        Long userEmployeeCode = 12L;

        Map<String, Object> resultMap = messengerService.selectChatroomList(userEmployeeCode);

        return res("채팅방 목록 식별 코드와 안읽은 채팅 존재 여부 반환", resultMap);
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