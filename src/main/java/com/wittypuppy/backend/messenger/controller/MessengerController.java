package com.wittypuppy.backend.messenger.controller;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.common.dto.Criteria;
import com.wittypuppy.backend.common.dto.PageDTO;
import com.wittypuppy.backend.common.dto.PagingResponseDTO;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.messenger.dto.*;
import com.wittypuppy.backend.messenger.service.MessengerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/messenger")
@AllArgsConstructor
public class MessengerController {
    private final MessengerService messengerService;

    /**
     * front 측면에서 로그인에 성공할 경우 채팅방 목록을 가져온 다음에<br>
     * 이 채팅방 목록의 코드값을 이용해서 handShake를 먼저 진행해준다.<br>
     * 말그대로 내가 속한 채팅방의 식별 코드.
     * <p>
     * 그리고 남은 채팅이 있는지 여부를 가져온다.
     *
     * @param principal 계정 정보
     * @return 200, 메시지, 데이터 반환 (데이터: 내가 속한 채팅방 목록, 안읽은 채팅 내역)
     */
    @GetMapping("/login-settings")
    public ResponseEntity<ResponseDTO> selectChatroomCodeListAndIsAlert(
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();

        ChatroomInfo chatroomInfo = messengerService.selectChatroomList(userEmployeeCode);

        return res("채팅방 목록 식별 코드와 안읽은 채팅 존재 여부 반환", chatroomInfo);
    }

    /**
     * 메신저 메인화면을 가져온다. 메신저 옵션과 채팅방 목록에 관련된 정보가 들어 있다.
     *
     * @param principal 계정 정보
     * @return 200, 메시지, 메신저 메인화면 관련 DTO
     */
    @GetMapping("/chatrooms")
    public ResponseEntity<ResponseDTO> openMessenger(
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        MessengerMainDTO messengerMainDTO = messengerService.openMessenger(userEmployeeCode);
        return res("메신저 메인화면 반환", messengerMainDTO);
    }

    /**
     * 메신저 옵션 화면을 가져온다. 옵션을 수정하기 전에 가져와야 한다.
     *
     * @param principal 계정 정보
     * @return 200, 메시지, 메신저 옵션 관련 DTO
     */
    @GetMapping("/options")
    public ResponseEntity<ResponseDTO> openMessengerOptions(
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        MessengerOptionsDTO messengerOptionsDTO = messengerService.openMessengerOptions(userEmployeeCode);
        return res("메신저 옵션 반환", messengerOptionsDTO);
    }

    /**
     * 메신저 옵션을 저장한다.
     *
     * @param messengerOptionsDTO 메신저 옵션 관련 DTO
     * @param principal           계정 정보
     * @return 200, 성공 여부 메시지
     */
    @PutMapping("/options")
    public ResponseEntity<ResponseDTO> modifyMessengerOptions(
            @RequestBody MessengerOptionsDTO messengerOptionsDTO,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("메신저 옵션 수정 성공", messengerService.modifyMessengerOptions(messengerOptionsDTO, userEmployeeCode));
    }

    /**
     * 채팅방에 고정 여부를 변경한다.
     *
     * @param chatroomCode
     * @param principal
     * @return
     */
    @PutMapping("/chatrooms/{chatroomCode}")
    public ResponseEntity<ResponseDTO> pinnedChatroom(
            @PathVariable Long chatroomCode,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("메신저 고정여부 수정 성공", messengerService.pinnedChatroom(chatroomCode, userEmployeeCode));
    }

    /**
     * 채팅방 만들기. 채팅방 정보로는 제목과 고정여부가 들어간다.<br>
     * 이때 고정 여부는 제작하는 사람이 관리자가 되면서 동시에 관리자의 채팅방 설정을 의미한다.
     *
     * @param chatroomOptions
     * @param principal
     * @return
     */
    @PostMapping("/chatrooms")
    public ResponseEntity<ResponseDTO> createChatroom(
            @RequestBody ChatroomOptionsDTO chatroomOptions,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("채팅방 생성 성공", messengerService.createChatroom(chatroomOptions, userEmployeeCode));
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
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        ChatroomMainDTO chatroomMainDTO = messengerService.openChatroom(chatroomCode, userEmployeeCode);
        return res("채팅방 접속 성공", chatroomMainDTO);
    }

    @GetMapping("/chatrooms/{chatroomCode}/paging/{requestChatCode}")
    public ResponseEntity<ResponseDTO> selectChatListWithScrollPaging(
            @PathVariable Long chatroomCode,
            @PathVariable Long requestChatCode,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        List<ChatDTO> chatDTOList = messengerService.selectChatListWithScrollPaging(chatroomCode, requestChatCode, userEmployeeCode);
        return res("채팅방 목록 반환", chatDTOList);
    }

    @GetMapping("/chatrooms/{chatroomCode}/options")
    public ResponseEntity<ResponseDTO> openChatroomOptions(
            @PathVariable Long chatroomCode,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        ChatroomOptionsDTO chatroomOptionsDTO = messengerService.openChatroomOptions(chatroomCode, userEmployeeCode);
        return res("채팅방 옵션 반환", chatroomOptionsDTO);
    }

    @PutMapping("/chatrooms/{chatroomCode}/options")
    public ResponseEntity<ResponseDTO> modifyChatroomOptions(
            @PathVariable Long chatroomCode,
            @RequestBody ChatroomOptionsDTO chatroomOptionsDTO,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res(messengerService.modifyChatroomOptions(chatroomCode, chatroomOptionsDTO, userEmployeeCode));
    }

    @GetMapping("/employees")
    public ResponseEntity<ResponseDTO> selectEmployees(
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("사원 목록 가져오기 성공", messengerService.selectEmployees());
    }

    @PutMapping("/chatrooms/{chatroomCode}/invite/{inviteEmployeeCode}")
    public ResponseEntity<ResponseDTO> inviteEmployees(
            @PathVariable Long chatroomCode,
            @PathVariable Long inviteEmployeeCode,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("채팅방 초대 성공", messengerService.inviteEmployee(chatroomCode, inviteEmployeeCode, userEmployeeCode));
    }

    @PutMapping("/chatrooms/{chatroomCode}/delegate-admin")
    public ResponseEntity<ResponseDTO> delegateChatroomAdmin(
            @PathVariable Long chatroomCode,
            @RequestParam Long delegateChatroomMemberCode,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res(messengerService.delegateChatroomAdmin(delegateChatroomMemberCode, chatroomCode, userEmployeeCode));
    }

    @GetMapping("/chatrooms/{chatroomCode}/profile")
    public ResponseEntity<ResponseDTO> findProfileImage(
            @PathVariable Long chatroomCode,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("프로필사진 이미지 url 가져오기 성공", messengerService.findProfileImage(chatroomCode, userEmployeeCode));
    }

    @GetMapping("/chatrooms/{chatroomCode}/search")
    public ResponseEntity<ResponseDTO> findChatList(
            @PathVariable Long chatroomCode,
            @RequestParam(name = "search", required = false, defaultValue = "") String searchValue,
            @RequestParam(name = "offset", defaultValue = "1") String offset,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        Criteria cri = new Criteria(Integer.valueOf(offset), 20);
        Map<String, Object> result = messengerService.findChatList(chatroomCode, "%" + searchValue + "%", userEmployeeCode, cri);
        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(result.get("content"));
        pagingResponseDTO.setPageInfo(new PageDTO(cri, ((Long) result.get("totalSize")).intValue(), 3));
        return res("채팅방 검색 결과 반환", pagingResponseDTO);
    }

    @PutMapping("/chatrooms/{chatroomCode}/profile")
    public ResponseEntity<ResponseDTO> updateProfileImage(
            @PathVariable Long chatroomCode,
            MultipartFile file,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        if (file == null || file.isEmpty()) {
            return res("해당 파일이 존재하지 않습니다.");
        }
        return res("프로필 변경 성공", messengerService.updateProfileImage(chatroomCode, file, userEmployeeCode));
    }

    @DeleteMapping("/chatrooms/{chatroomCode}/kicked-member")
    public ResponseEntity<ResponseDTO> kickChatroomMember(
            @PathVariable Long chatroomCode,
            @RequestParam Long kickChatroomMemberCode,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res(messengerService.kickChatroomMember(kickChatroomMemberCode, chatroomCode, userEmployeeCode));
    }

    @DeleteMapping("/chatrooms/{chatroomCode}/leave")
    public ResponseEntity<ResponseDTO> leaveChatroomMember(
            @PathVariable Long chatroomCode,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("채팅방 나가기 성공", messengerService.leaveChatroomMember(chatroomCode, userEmployeeCode));
    }

    @PutMapping("/chatrooms/{chatroomCode}/read-status-update")
    public ResponseEntity<ResponseDTO> updateChatReadStatus(
            @PathVariable Long chatroomCode,
            @AuthenticationPrincipal User principal) {
        Long userEmployeeCode = (long) principal.getEmployeeCode();
        return res("최근 채팅 관찰 시점 갱신 성공", messengerService.updateChatReadStatus(chatroomCode, userEmployeeCode));
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