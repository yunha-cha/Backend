package com.wittypuppy.backend.messenger.service;

import com.wittypuppy.backend.common.exception.DataInsertionException;
import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.common.exception.DataUpdateException;
import com.wittypuppy.backend.messenger.dto.*;
import com.wittypuppy.backend.messenger.entity.*;
import com.wittypuppy.backend.messenger.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessengerService {
    private final ChatFileRepository chatFileRepository;
    private final ChatReadStatusRepository chatReadStatusRepository;
    private final ChatRepository chatRepository;
    private final ChatroomMemberRepository chatroomMemberRepository;
    private final ChatroomProfileRepository chatroomProfileRepository;
    private final ChatroomRepository chatroomRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final JobRepository jobRepository;
    private final MessengerRepository messengerRepository;
    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;

    /* 메신저 열기 */
    @Transactional
    public MessengerMainDTO openMessenger(Long userEmployeeCode) {
        Messenger messenger = messengerRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                .orElseGet(() -> {
                    Employee employee = employeeRepository.findById(userEmployeeCode)
                            .orElseThrow(() -> new DataNotFoundException("사원 정보가 없습니다."));
                    Messenger newMessenger = new Messenger()
                            .setEmployee(employee)
                            .setMessengerOption("RD") // LU RU LD RD (각각 좌상, 우상, 좌하, 우하)
                            .setMessengerMiniAlarmOption("N")
                            .setMessengerTheme("DEFAULT")
                            .builder();

                    try {
                        messengerRepository.save(newMessenger);
                    } catch (Exception e) {
                        throw new DataInsertionException("메신저 정보가 존재하지 않아 추가하려 했으나 실패했습니다.");
                    }
                    return newMessenger;
                });
        List<Chatroom> chatroomList = messenger.getChatroomList();

        MessengerMainDTO messengerMainDTO = new MessengerMainDTO()
                .setMessengerCode(messenger.getMessengerCode())
                .setMessengerOption(messenger.getMessengerOption())
                .setMessengerMiniAlarmOption(messenger.getMessengerMiniAlarmOption())
                .setMessengerTheme(messenger.getMessengerTheme())
                .builder();

        if (chatroomList.size() == 0) {
            return messengerMainDTO;
        }
        // 채팅방이 조금이라도 있을 경우
        List<ChatroomMessengerMainDTO> chatroomMessengerMainDTOList = messengerRepository.getMessengerStatistics(userEmployeeCode);
        messengerMainDTO.setChatroomList(chatroomMessengerMainDTOList);

        return messengerMainDTO;
    }

    /* 메신저 옵션창 열기 */
    public MessengerOptionsDTO openMessengerOptions(Long userEmployeeCode) {
        Messenger messenger = messengerRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정의 메신저 정보가 없습니다."));
        MessengerOptionsDTO messengerOptionsDTO = new MessengerOptionsDTO()
                .setMessengerPositionOption(messenger.getMessengerOption())
                .setMessengerMiniAlarmOption(messenger.getMessengerMiniAlarmOption())
                .setMessengerTheme(messenger.getMessengerTheme())
                .builder();

        return messengerOptionsDTO;
    }

    /* 메신저 옵션 저장하기*/
    @Transactional
    public String saveMessengerOptions(MessengerOptionsDTO messengerOptionsDTO, Long userEmployeeCode) {
        Messenger messenger = messengerRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정의 메신저 정보가 없습니다."));
        try {
            messenger = messenger
                    .setMessengerOption(messengerOptionsDTO.getMessengerPositionOption())
                    .setMessengerMiniAlarmOption(messengerOptionsDTO.getMessengerMiniAlarmOption())
                    .setMessengerTheme(messengerOptionsDTO.getMessengerTheme())
                    .builder();
        } catch (Exception e) {
            throw new DataUpdateException("메신저 옵션 수정 실패");
        }
        return "메신저 옵션 수정 성공";
    }

    /* 채팅방 고정하기 */
    @Transactional
    public String pinnedChatroom(Long chatroomCode, Long userEmployeeCode) {
        ChatroomMember chatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 선택한 채팅방이 존재하지 않습니다."));
        try {
            if (chatroomMember.getChatroomMemberPinnedStatus().equals("N")) {
                chatroomMember = chatroomMember
                        .setChatroomMemberPinnedStatus("Y")
                        .builder();
            } else {
                chatroomMember = chatroomMember
                        .setChatroomMemberPinnedStatus("N")
                        .builder();
            }
        } catch (Exception e) {
            throw new DataUpdateException("메신저 고정/고정해제 실패");
        }

        return "메신저 고정/고정해제 성공";
    }


    /* 채팅방 만들기 */
    @Transactional
    public String createChatroom(ChatroomOptionsDTO chatroomOptionsDTO, Long userEmployeeCode) {
        /*
         * 해야할 작업
         *
         * 역순으로 고려해야됨
         * 메신저 코드로
         * 채팅방 만들기
         * 채팅방 프사 만들기 null인 상태로 데이터만
         * 채팅방 멤버 만들기 1명
         * 채팅 관찰 만들기. 채팅코드는 null 상태
         * */
        String chatroomTitle = chatroomOptionsDTO.getChatroomTitle();
        LocalDateTime now = LocalDateTime.now();
        Employee employee = employeeRepository.findById(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정 정보가 존재하지 않습니다."));
        ChatroomMember chatroomMember = new ChatroomMember()
                .setEmployee(employee)
                .setChatroomMemberType("관리자")
                .setChatroomMemberInviteTime(now)
                .setChatroomMemberPinnedStatus("N")
                .builder();
        ChatroomProfile chatroomProfile = new ChatroomProfile()
                .setChatroomProfileRegistDate(now)
                .setChatroomProfileDeleteStatus("N")
                .builder();
        ChatReadStatus chatReadStatus = new ChatReadStatus()
                .builder();
        Chatroom chatroom = new Chatroom()
                .setChatroomTitle(chatroomTitle)
                .setChatroomFixedStatus(null)
                .setChatroomMemberList(Collections.singletonList(chatroomMember))
                .setChatroomProfileList(Collections.singletonList(chatroomProfile))
                .setChatReadStatusList(Collections.singletonList(chatReadStatus))
                .builder();
        try {
            chatroomRepository.save(chatroom);
        } catch (Exception e) {
            throw new DataInsertionException("채팅방 생성 실패");
        }
        return "채팅방 생성 성공";
    }

    /* 채팅방 접속하기 */
    public ChatroomMainDTO openChatroom(Long chatroomCode, Long userEmployeeCode) {
        Chatroom chatroom = chatroomRepository.findByChatroomCodeAndChatroomMemberList_Employee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
        ChatroomMainDTO chatroomMainDTO = modelMapper.map(chatroom, ChatroomMainDTO.class);
        Long memberCount = chatroomMemberRepository.getChatroomMemberCount(chatroomCode);
        ChatroomMember chatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
        ChatReadStatus chatReadStatus = chatReadStatusRepository.findByChatroomCodeAndChatroomMemberCode(chatroomCode, chatroomMember.getChatroomMemberCode())
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
        ChatroomProfile chatroomProfile = chatroomProfileRepository.findByChatroomCodeAndChatroomProfileDeleteStatus(chatroomCode, "N")
                .orElseThrow(() -> new DataNotFoundException("채팅방 프로필 사진을 가져오지 못했습니다."));

        chatroomMainDTO.setLastReadChatCode(chatReadStatus.getChatCode());
        chatroomMainDTO.setChatroomProfileFileURL(chatroomProfile.getChatroomProfileChangedFile());
        chatroomMainDTO.setChatroomMemberCount(memberCount);

        return chatroomMainDTO;
    }

    /* 채팅방 옵션창 열기 */
    public ChatroomOptionsDTO openChatroomOptions(Long chatroomCode, Long userEmployeeCode) {
        Chatroom chatroom = chatroomRepository.findByChatroomCodeAndChatroomMemberList_Employee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
        ChatroomMember chatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
        ChatroomOptionsDTO chatroomOptionsDTO = new ChatroomOptionsDTO()
                .setChatroomCode(chatroom.getChatroomCode())
                .setChatroomTitle(chatroom.getChatroomTitle())
                .setChatroomFixedStatus(chatroomMember.getChatroomMemberPinnedStatus())
                .builder();
        return chatroomOptionsDTO;
    }

    /* 채팅방 옵션 저장하기 */
    @Transactional
    public String saveChatroomOptions(ChatroomOptionsDTO chatroomOptionsDTO, Long userEmployeeCode) {
        Long chatroomCode = chatroomOptionsDTO.getChatroomCode();
        String chatroomTitle = chatroomOptionsDTO.getChatroomTitle();
        String chatroomFixedStatus = chatroomOptionsDTO.getChatroomFixedStatus();
        Chatroom chatroom = chatroomRepository.findByChatroomCodeAndChatroomMemberList_Employee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
        ChatroomMember chatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
        chatroomMember = chatroomMember.setChatroomMemberPinnedStatus(chatroomFixedStatus);
        if (chatroomMember.getChatroomMemberType().equals("관리자")) {
            chatroom = chatroom.setChatroomTitle(chatroomTitle).builder();
        } else {
            throw new DataInsertionException("해당 계정은 채팅방 관리자가 아니어서 채팅방 고정여부만 변경됩니다.");
        }
        return "채팅방 옵션 변경 성공";
    }

    /* 전체 사원 목록 출력하기 */
    public void selectEmployees() {
        List<Employee> employeeList = employeeRepository.findAllByEmployeeRetirementDateIsNull();

    }

    /* 채팅방 초대하기 */
    @Transactional
    public void inviteEmployees(List<Long> inviteEmployeeCodeList, Long chatroomCode, Long userEmployeeCode) {
        ChatroomMember userChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 계정 정보가 없습니다."));
        if (!userChatroomMember.getChatroomMemberType().equals("관리자")) {
            return;
        }
        // 내가 관리자인 경우에 초대가 가능하다.
        List<ChatroomMember> existChatroomMemberList = chatroomMemberRepository.findAllByChatroomCodeAndChatroomMemberTypeIn(chatroomCode, List.of("관리자", "일반사원"));
        List<Long> existChatroomMemberEmployeeCodeList = existChatroomMemberList.stream().map(chatroomMember -> chatroomMember.getEmployee().getEmployeeCode())
                .collect(Collectors.toList());
        inviteEmployeeCodeList.removeAll(existChatroomMemberEmployeeCodeList);
        LocalDateTime now = LocalDateTime.now();
        inviteEmployeeCodeList.forEach(inviteEmployeeCode -> {
            Employee employee = employeeRepository.findById(inviteEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("초대할 사원이 존재하지 않습니다."));
            ChatroomMember newChatroomMember = new ChatroomMember()
                    .setChatroomCode(chatroomCode)
                    .setEmployee(employee)
                    .setChatroomMemberType("일반사원")
                    .setChatroomMemberInviteTime(now)
                    .builder();
            chatroomMemberRepository.save(newChatroomMember);
        });
    }

    /* 관리자 위임하기 */
    @Transactional
    public void delegateChatroomAdmin(Long delegateChatroomMemberCode, Long chatroomCode, Long userEmployeeCode) {
        ChatroomMember userChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 로그인한 계정 정보가 없습니다."));
        if (!userChatroomMember.getChatroomMemberType().equals("관리자")) {
            return;
        }
        ChatroomMember delegateChatroomMember = chatroomMemberRepository.findByChatroomMemberCodeAndChatroomCode(chatroomCode, delegateChatroomMemberCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 관리자를 위임할 계정 정보가 없습니다."));
        if (!delegateChatroomMember.getChatroomMemberType().equals("일반사원")) {
            return;
        }
        delegateChatroomMember = delegateChatroomMember.setChatroomMemberType("관리자").builder();
        userChatroomMember = userChatroomMember.setChatroomMemberType("일반사원").builder();
    }
    /* 채팅방 프로필 사진 가져오기 */

    /* 채팅방 프로필 사진 변경하기 */

    /* 채팅방 내보내기 */
    @Transactional
    public void kickChatroomMember(Long kickChatroomMemberCode, Long chatroomCode, Long userEmployeeCode) {
        ChatroomMember userChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 로그인한 계정 정보가 없습니다."));
        if (!userChatroomMember.getChatroomMemberType().equals("관리자")) {
            return;
        }
        ChatroomMember kickChatroomMember = chatroomMemberRepository.findByChatroomMemberCodeAndChatroomCode(chatroomCode, kickChatroomMemberCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 관리자를 위임할 계정 정보가 없습니다."));
        kickChatroomMember = kickChatroomMember
                .setChatroomMemberType("삭제")
                .builder();
    }

    /* 채팅방 나가기 */
    @Transactional
    public void exitChatroomMember(Long chatroomCode, Long userEmployeeCode) {
        ChatroomMember userChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 로그인한 계정 정보가 없습니다."));
        if (!userChatroomMember.getChatroomMemberType().equals("관리자")) {
            return;
        }
        List<ChatroomMember> chatroomMemberList = chatroomMemberRepository.findAllByChatroomCodeAndChatroomMemberTypeIn(chatroomCode, List.of("일반사원", "관리자"));
        if (chatroomMemberList.size() <= 1) {
            Chatroom chatroom = chatroomRepository.findById(chatroomCode)
                    .orElseThrow(() -> new DataNotFoundException("관리자 한명만 남아서 채팅방을 삭제하려 했으나 채팅방 정보를 찾을 수 없습니다."));
            chatroomRepository.delete(chatroom);
            return;
        }
        // 관리자도 아니고 그렇다고 아직 2명이상 있으면
        userChatroomMember = userChatroomMember.setChatroomMemberType("삭제")
                .builder();
    }

    /* 채팅 입력하기 */
    @Transactional
    public void createChat(String chatContent, Long chatroomCode, Long userEmployeeCode) {
        Chatroom chatroom = chatroomRepository.findByChatroomCodeAndChatroomMemberList_Employee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정 정보가 해당 채팅방에 없습니다."));
        ChatroomMember userChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 로그인한 계정 정보가 없습니다."));
        LocalDateTime now = LocalDateTime.now();
        Chat chat = new Chat()
                .setChatroomCode(chatroom.getChatroomCode())
                .setChatroomMemberCode(userChatroomMember.getChatroomMemberCode())
                .setChatWriteDate(now)
                .setChatContent(chatContent)
                .builder();
        chatRepository.save(chat);
    }

    /* 채팅 사진 입력하기 */

    /* 채팅 관찰 시점 업데이트*/
    @Transactional
    public void updateChatReadStatus(Long chatCode, Long chatroomCode, Long userEmployeeCode) {
        Chatroom chatroom = chatroomRepository.findByChatroomCodeAndChatroomMemberList_Employee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정 정보가 해당 채팅방에 없습니다."));
        ChatroomMember userChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 로그인한 계정 정보가 없습니다."));
        Chat readChat = chatRepository.findById(chatCode)
                .orElseThrow(() -> new DataNotFoundException("현재 발생한 채팅에 대한 정보가 없습니다."));

        ChatReadStatus chatReadStatus = chatReadStatusRepository.findByChatCodeAndChatroomMemberCode(readChat.getChatCode(), userChatroomMember.getChatroomMemberCode())
                .orElseGet(() -> {
                    ChatReadStatus newChatReadStatus = new ChatReadStatus()
                            .setChatroomMemberCode(userChatroomMember.getChatroomMemberCode())
                            .builder();
                    chatReadStatusRepository.save(newChatReadStatus);
                    return newChatReadStatus;
                });
        chatReadStatus.setChatCode(chatCode);
    }
}