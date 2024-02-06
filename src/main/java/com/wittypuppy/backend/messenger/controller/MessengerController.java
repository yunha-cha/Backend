package com.wittypuppy.backend.messenger.controller;

import com.wittypuppy.backend.Employee.dto.EmployeeDTO;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.messenger.dto.*;
import com.wittypuppy.backend.messenger.service.MessengerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messenger")
@AllArgsConstructor
@Slf4j
public class MessengerController {
    private final MessengerService messengerService;

    /**
     * front 측면에서 로그인에 성공할 경우 채팅방 목록을 가져온 다음에<br>
     * 이 채팅방 목록의 코드값을 이용해서 handShake를 먼저 진행해준다.<br>
     * 말그대로 내가 속한 채팅방의 식별 코드.
     * <p>
     * 그리고 남은 채팅이 있는지 여부를 가져온다.
     *
     * @param object 계정 정보
     * @return 200, 메시지, 데이터 반환 (데이터: 내가 속한 채팅방 목록, 안읽은 채팅 내역)
     */
    @GetMapping("/login-settings")
    public ResponseEntity<ResponseDTO> selectChatroomCodeListAndIsAlert(
            @AuthenticationPrincipal Object object) {
        Long userEmployeeCode = 12L;

        ChatroomInfo chatroomInfo = messengerService.selectChatroomList(userEmployeeCode);

        return res("채팅방 목록 식별 코드와 안읽은 채팅 존재 여부 반환", chatroomInfo);
    }

    /**
     * 메신저 메인화면을 가져온다. 메신저 옵션과 채팅방 목록에 관련된 정보가 들어 있다.
     *
     * @param object 계정 정보
     * @return 200, 메시지, 메신저 메인화면 관련 DTO
     */
    @GetMapping("/chatrooms")
    public ResponseEntity<ResponseDTO> openMessenger(
            @AuthenticationPrincipal Object object) {
        Long userEmployeeCode = 12L;
        MessengerMainDTO messengerMainDTO = messengerService.openMessenger(userEmployeeCode);
        return res("메신저 메인화면 반환", messengerMainDTO);
    }

    /**
     * 메신저 옵션 화면을 가져온다. 옵션을 수정하기 전에 가져와야 한다.
     *
     * @param object 계정 정보
     * @return 200, 메시지, 메신저 옵션 관련 DTO
     */
    @GetMapping("/options")
    public ResponseEntity<ResponseDTO> openMessengerOptions(
            @AuthenticationPrincipal Object object) {
        Long userEmployeeCode = 12L;
        MessengerOptionsDTO messengerOptionsDTO = messengerService.openMessengerOptions(userEmployeeCode);
        return res("메신저 옵션 반환", messengerOptionsDTO);
    }

    /**
     * 메신저 옵션을 저장한다.
     *
     * @param messengerOptionsDTO 메신저 옵션 관련 DTO
     * @param object              계정 정보
     * @return 200, 성공 여부 메시지
     */
    @PutMapping("/options")
    public ResponseEntity<ResponseDTO> modifyMessengerOptions(
            @RequestBody MessengerOptionsDTO messengerOptionsDTO,
            @AuthenticationPrincipal Object object) {
        Long userEmployeeCode = 12L;
        return res(messengerService.modifyMessengerOptions(messengerOptionsDTO, userEmployeeCode));
    }

    /**
     * 채팅방에 고정 여부를 변경한다.
     *
     * @param chatroomCode
     * @param object
     * @return
     */
    @PutMapping("/chatrooms/{chatroomCode}")
    public ResponseEntity<ResponseDTO> pinnedChatroom(
            @PathVariable Long chatroomCode,
            @AuthenticationPrincipal Object object) {
        Long userEmployeeCode = 12L;
        return res(messengerService.pinnedChatroom(chatroomCode, userEmployeeCode));
    }

    /**
     * 채팅방 만들기. 채팅방 정보로는 제목과 고정여부가 들어간다.<br>
     * 이때 고정 여부는 제작하는 사람이 관리자가 되면서 동시에 관리자의 채팅방 설정을 의미한다.
     *
     * @param chatroomOptionsDTO
     * @param principal
     * @return
     */
    @PostMapping("/chatrooms")
    public ResponseEntity<ResponseDTO> createChatroom(
            @RequestBody ChatroomOptionsDTO chatroomOptionsDTO,
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = 12L;
        return res(messengerService.createChatroom(chatroomOptionsDTO, userEmployeeCode));
    }

    /**
     * 채팅방 내부 정보 반환. 채팅은 따로 가져와야된다.
     *
     * @param chatroomCode
     * @param principal
     * @return
     */
    @GetMapping("/chatrooms/{chatroomCode}")
    public ResponseEntity<ResponseDTO> openChatroom(
            @PathVariable Long chatroomCode,
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = 12L;
        ChatroomMainDTO chatroomMainDTO = messengerService.openChatroom(chatroomCode, userEmployeeCode);
        return res("채팅방 접속 성공", chatroomMainDTO);
    }

    @GetMapping("/chatrooms/{chatroomCode}/paging")
    public ResponseEntity<ResponseDTO> selectChatListWithScrollPaging(
            @PathVariable Long chatroomCode,
            @RequestParam Long requestChatCode,
            @RequestParam String direction,
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = 12L;
        List<ChatDTO> chatDTOList = messengerService.selectChatListWithScrollPaging(chatroomCode, requestChatCode, direction);
        return res("채팅방 목록 반환", chatDTOList);
    }

    @GetMapping("/chatrooms/{chatroomCode}/options")
    public ResponseEntity<ResponseDTO> openChatroomOptions(
            @PathVariable Long chatroomCode,
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = 12L;
        ChatroomOptionsDTO chatroomOptionsDTO = messengerService.openChatroomOptions(chatroomCode, userEmployeeCode);
        return res("채팅방 옵션 반환", chatroomOptionsDTO);
    }

    @PutMapping("/chatrooms/{chatroomCode}/options")
    public ResponseEntity<ResponseDTO> modifyChatroomOptions(
            @PathVariable Long chatroomCode,
            @RequestBody ChatroomOptionsDTO chatroomOptionsDTO,
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = 12L;
        return res(messengerService.modifyChatroomOptions(chatroomCode, chatroomOptionsDTO, userEmployeeCode));
    }

    @GetMapping("/employees")
    public ResponseEntity<ResponseDTO> selectEmployees(
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = 12L;
        return res("사원 목록 가져오기 성공", messengerService.selectEmployees());
    }

    @PutMapping("/chatrooms/{chatroomCode}/invite")
    public ResponseEntity<ResponseDTO> inviteEmployees(
            @PathVariable Long chatroomCode,
            @RequestBody List<Long> inviteEmployeeCodeList,
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = 12L;
        return res(messengerService.inviteEmployees(chatroomCode, inviteEmployeeCodeList, userEmployeeCode));
    }

    @GetMapping("/chatrooms/{chatroomCode}/members")
    public ResponseEntity<ResponseDTO> inviteEmployees(
            @PathVariable Long chatroomCode,
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = 12L;
        return res("현재 채팅방에 있는 멤버 조회 성공", messengerService.selectChatroomMember(chatroomCode, userEmployeeCode));
    }

    @PutMapping("/chatrooms/{chatroomCode}/delegate-admin")
    public ResponseEntity<ResponseDTO> delegateChatroomAdmin(
            @PathVariable Long chatroomCode,
            @RequestParam Long delegateChatroomMemberCode,
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = 12L;
        return res(messengerService.delegateChatroomAdmin(delegateChatroomMemberCode, chatroomCode, userEmployeeCode));
    }

    @GetMapping("/chatrooms/{chatroomCode}/profile")
    public ResponseEntity<ResponseDTO> findProfileImage(
            @PathVariable Long chatroomCode,
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = 12L;
        return res("프로필사진 이미지 url 가져오기 성공", messengerService.findProfileImage(chatroomCode, userEmployeeCode));
    }


    @PutMapping("/chatrooms/{chatroomCode}/profile")
    public ResponseEntity<ResponseDTO> updateProfileImage(
            @PathVariable Long chatroomCode,
            MultipartFile chatroomProfileImage,
            @AuthenticationPrincipal EmployeeDTO principal) {
        if (chatroomProfileImage.isEmpty()) {
            return res("해당 파일이 존재하지 않습니다.");
        }
        Long userEmployeeCode = 12L;
        return res(messengerService.updateProfileImage(chatroomCode, chatroomProfileImage, userEmployeeCode));
    }

    @DeleteMapping("/chatrooms/{chatroomCode}/kicked-member")
    public ResponseEntity<ResponseDTO> kickChatroomMember(
            @PathVariable Long chatroomCode,
            @RequestParam Long kickChatroomMemberCode,
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = 12L;
        return res(messengerService.kickChatroomMember(kickChatroomMemberCode, chatroomCode, userEmployeeCode));
    }

    @PutMapping("/chatrooms/{chatroomCode}/exit")
    public ResponseEntity<ResponseDTO> exitChatroomMember(
            @PathVariable Long chatroomCode,
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = 12L;
        return res(messengerService.exitChatroomMember(chatroomCode, userEmployeeCode));
    }

    @PutMapping("/chatrooms/{chatroomCode}/read-status-update")
    public ResponseEntity<ResponseDTO> updateChatReadStatus(
            @PathVariable Long chatroomCode,
            @RequestParam Long chatCode,
            @AuthenticationPrincipal EmployeeDTO principal) {
        Long userEmployeeCode = 12L;
        return res(messengerService.updateChatReadStatus(chatCode, chatroomCode, userEmployeeCode));
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