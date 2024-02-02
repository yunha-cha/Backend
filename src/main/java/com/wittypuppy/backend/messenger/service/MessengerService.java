package com.wittypuppy.backend.messenger.service;

import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.messenger.dto.MessengerOptionsDTO;
import com.wittypuppy.backend.messenger.entity.*;
import com.wittypuppy.backend.messenger.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Stream;

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

    /* 채팅방 옵션창 열기 */

    /* 채팅방 옵션 저장하기 */

    /* 사원 목록 출력하기 */

    /* 채팅방 초대하기 */

    /* 관리자 위임하기 */

    /* 채팅방 내보내기 */

    /* 채팅방 나가기 */
}
