package com.wittypuppy.backend.messenger.service;

import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.messenger.dto.ChatroomOptionsDTO;
import com.wittypuppy.backend.messenger.dto.MessengerOptionsDTO;
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
    public void openMessenger(Long userEmployeeCode) {
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
                    messengerRepository.save(newMessenger);
                    return newMessenger;
                });
    }

    /* 채팅 불러오기*/
    public void readChatList(Long chatroomCode, Long userEmployeeCode) {
        Chatroom chatroom = chatroomRepository.findByChatroomCodeAndChatroomMemberList_Employee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
        ChatroomMember userChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 계정 정보가 없습니다."));
        List<Chat> chatList = chatRepository.findAllByChatroomCodeAndChatroomMemberCode(chatroomCode, userChatroomMember.getChatroomMemberCode());
    }

    /* 메신저 옵션창 열기 */
    public void openMessengerOptions(Long userEmployeeCode) {
        Messenger messenger = messengerRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정의 메신저 정보가 없습니다."));
    }

    /* 메신저 옵션 저장하기*/
    @Transactional
    public void saveMessengerOptions(MessengerOptionsDTO messengerOptionsDTO, Long userEmployeeCode) {
        Messenger messenger = messengerRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정의 메신저 정보가 없습니다."));
        messenger = messenger
                .setMessengerOption(messengerOptionsDTO.getMessengerOption())
                .setMessengerMiniAlarmOption(messengerOptionsDTO.getMessengerMiniAlarmOption())
                .setMessengerTheme(messengerOptionsDTO.getMessengerTheme())
                .builder();
    }

    /* 채팅방 고정하기 */
    @Transactional
    public void pinnedChatroom(Long chatroomCode, Long userEmployeeCode) {
        Messenger messenger = messengerRepository.findByEmployee_EmployeeCodeAndChatroomList_ChatroomCode(userEmployeeCode, chatroomCode)
                .orElseThrow(() -> new DataNotFoundException("현재 선택한 채팅방이 존재하지 않습니다.")); // 계정 정보로 확인
        Chatroom chatroom = chatroomRepository.findById(chatroomCode)
                .orElseThrow(() -> new DataNotFoundException("현재 선택한 채팅방이 존재하지 않습니다.")); // 실제 데이터 다시 가져오기
        if (chatroom.getChatroomFixedStatus().equals("N")) {
            chatroom = chatroom
                    .setChatroomFixedStatus("Y")
                    .builder();
        } else {
            chatroom = chatroom
                    .setChatroomFixedStatus("N")
                    .builder();
        }
    }

    /* 채팅방 만들기 - 내가 관리자가 되어야 한다. */
    @Transactional
    public void createChatroom(Long chatroomCode, String chatroomTitle, Long userEmployeeCode) {
        Messenger messenger = messengerRepository.findByEmployee_EmployeeCodeAndChatroomList_ChatroomCode(userEmployeeCode, chatroomCode)
                .orElseThrow(() -> new DataNotFoundException("현재 선택한 채팅방이 존재하지 않습니다.")); // 계정 정보로 확인
        Long messengerCode = messenger.getMessengerCode();
        Employee employee = employeeRepository.findById(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정 정보를 가져올 수 없습니다."));
        LocalDateTime now = LocalDateTime.now();
        ChatroomMember chatroomMember = new ChatroomMember()
                .setEmployee(employee)
                .setChatroomMemberType("관리자")
                .setChatroomMemberInviteTime(now)
                .builder();
        ChatroomProfile chatroomProfile = new ChatroomProfile()
                .setChatroomProfileRegistDate(now)
                .setChatroomProfileDeleteStatus("N")
                .builder();
        Chatroom chatroom = new Chatroom()
                .setMessengerCode(messengerCode)
                .setChatroomTitle(chatroomTitle)
                .setChatroomFixedStatus("N")
                .setChatroomMemberList(Collections.singletonList(chatroomMember))
                .setChatroomProfileList(Collections.singletonList(chatroomProfile))
                .builder();
        chatroomRepository.save(chatroom);
    }

    /* 채팅방 접속하기 */
    public void openChatroom(Long chatroomCode, Long userEmployeeCode) {
        Chatroom chatroom = chatroomRepository.findByChatroomCodeAndChatroomMemberList_Employee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
    }

    /* 채팅방 옵션창 열기 */
    public void openChatroomOptions(Long chatroomCode, Long userEmployeeCode) {
        Chatroom chatroom = chatroomRepository.findByChatroomCodeAndChatroomMemberList_Employee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
    }

    /* 채팅방 옵션 저장하기 */
    @Transactional
    public void saveChatroomOptions(ChatroomOptionsDTO chatroomOptionsDTO, Long chatroomCode, Long userEmployeeCode) {
        Chatroom chatroom = chatroomRepository.findByChatroomCodeAndChatroomMemberList_Employee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
        chatroom = chatroom.setChatroomTitle(chatroomOptionsDTO.getChatroomTitle())
                .setChatroomFixedStatus(chatroomOptionsDTO.getChatroomFixedStatus())
                .builder();
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

        ChatReadStatus chatReadStatus = chatReadStatusRepository.findByChatroomCodeAndChatroomMemberCode(chatroomCode, userChatroomMember.getChatroomMemberCode())
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