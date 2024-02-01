package com.wittypuppy.backend.messenger.service;

import com.wittypuppy.backend.common.exception.DataInsertionException;
import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.messenger.dto.ChatroomOptionsDTO;
import com.wittypuppy.backend.messenger.dto.EmployeeDTO;
import com.wittypuppy.backend.messenger.dto.MessengerOptionsDTO;
import com.wittypuppy.backend.messenger.entity.*;
import com.wittypuppy.backend.messenger.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MessengerService {
    private final MessengerRepository messengerRepository;
    private final EmployeeRepository employeeRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatRepository chatRepository;
    private final ChatroomMemberRepository chatroomMemberRepository;
    private final ModelMapper modelMapper;

    /*
     * 메신저 열기
     * 메신저가 없을 경우 만들고
     * 메신저가 있으면 메신저/채팅방/채팅방프로필사진/채팅방멤버/사원을 들고 와야 된다.
     * 사원에는 당연히 부서, 프로필, 직급이 들어오고, 채팅방멤버도 상단 3개 정도는 읽어와야 된다.
     * 메신저를 열때 고정여부가 상단에 위치하도록 배치해야 된다.
     * 채팅읽음여부도 가지고 와야된다.
     * (어디까지 읽었고 거기서부터 지금까지 채팅이 몇개 있는지. 그리고 프론트 측에서는 구독을 하여서 데이터가 들어오면 1씩 증가해야 한다.)
     * */
    @Transactional
    public void selectMessenger(Long employeeCode) {
        Messenger messenger =
                messengerRepository.findByEmployee_EmployeeCode(employeeCode)
                        .orElseGet(() -> {
                            Messenger newMessenger = new Messenger();
                            Employee employee = employeeRepository.findByEmployeeCodeAndEmployeeRetirementDateIsNull(employeeCode)
                                    .orElseThrow(() -> new DataNotFoundException("현재 계정 정보를 찾을 수 없습니다."));
                            newMessenger = newMessenger.setEmployee(employee)
                                    .setMessengerOption("RD") // LU:좌상 LD:좌하 RU:우상 RD:우하
                                    .setMessengerMiniAlarmOption("N")
                                    .setMessengerTheme("DEFAULT")
                                    .builder();
                            messengerRepository.save(newMessenger);
                            return newMessenger;
                        });

    }

    /*
     * 메신저 설정 열기
     * 메신저 정보만 읽으면 된다.
     * */
    public MessengerOptionsDTO selectMessengerOptions(Long messengerCode, Long employeeCode) {
        Messenger messenger = messengerRepository.findByEmployee_EmployeeCode(employeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정의 메신저를 읽어올 수 없습니다."));
        if (messenger.getMessengerCode().equals(messengerCode)) {
            // 메신저 정보가 같은 경우에만 메신저 정보를 리턴해야 한다.
            MessengerOptionsDTO messengerOptionsDTO = new MessengerOptionsDTO();
            messengerOptionsDTO = messengerOptionsDTO.setMessengerOption(messenger.getMessengerOption())
                    .setMessengerMiniAlarmOption(messenger.getMessengerMiniAlarmOption())
                    .setMessengerTheme(messenger.getMessengerTheme())
                    .builder();
            return messengerOptionsDTO;
        }
        // 메신저 정보가 다를 경우에는 적절한 오류를 반환한다.
        return null;
    }

    /*
     * 메신저 설정 적용
     * 가져온 메신저 설정정보를 이용하여 update 하면 된다.
     * */
    @Transactional
    public String modifyMessengerOptions(MessengerOptionsDTO messengerOptionsDTO, Long messengerCode, Long employeeCode) {
        int result = 0;
        Messenger messenger = messengerRepository.findByEmployee_EmployeeCode(employeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정의 메신저를 읽어올 수 없습니다."));
        if (messenger.getMessengerCode().equals(messengerCode)) {
            // 메신저 정보가 현재 계정 정보와 일치하는 경우에만
            messenger = messenger.setMessengerOption(messengerOptionsDTO.getMessengerOption())
                    .setMessengerMiniAlarmOption(messengerOptionsDTO.getMessengerMiniAlarmOption())
                    .setMessengerTheme(messengerOptionsDTO.getMessengerTheme())
                    .builder();
            result = 1;
        }
        // 일치하지 않으면 오류 내뱉는다.
        return result > 0 ? "메신저 옵션 수정 성공" : "메신저 옵션 수정 실패";
    }

    /*
     * 채팅방 고정하기
     * 채팅방을 고정하면 즉시 update 됨.
     *
     * 고정풀기도 역시 동일함. 둘이 같은 코드를 사용할 수 있을거 같다. (마찬가지 async 하니깐 그 순간은 동기로 동작하므로)
     * */
    @Transactional
    public String pinChatroom(boolean isPin, Long chatroomCode, Long employeeCode) {
        int result = 0;
        Messenger messenger = messengerRepository.findByEmployee_EmployeeCode(employeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정의 메신저를 읽어올 수 없습니다."));
        Chatroom chatroom = chatroomRepository.findById(chatroomCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방 정보를 읽어올 수 없습니다."));
        if (messenger.getMessengerCode().equals(chatroom.getMessengerCode())) {
            if (isPin) {
                // 고정한다고 들어오면
                chatroom.setChatroomFixedStatus("Y");
                result = 1;
            } else {
                // 고정 해제한다고 들어오면
                chatroom.setChatroomFixedStatus("N");
                result = 2;
            }
        }
        return result == 2 ? "채팅방 고정 해제 성공" :
                result == 1 ? "채팅방 고정 성공" :
                        "채팅방 고정/고정해제 실패";
    }

    /*
     * 채팅방 만들기 창에서 사원 목록 가져오기
     * 채팅방 초대하기 창에서 사원 목록 가져오기
     * */
    public List<EmployeeDTO> selectEmployeeList() {
        List<Employee> employeeList = employeeRepository.findAllByEmployeeRetirementDateIsNull();
        List<EmployeeDTO> employeeDTOList = employeeList.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class)).toList();

        return employeeDTOList;
    }

    /*
     * 채팅방 만들기
     * 채팅방을 만든다.
     * */
    @Transactional
    public String createChatroom(ChatroomOptionsDTO chatroomOptionsDTO, Long employeeCode) {
        int result = 0;
        try {
            Messenger messenger = messengerRepository.findByEmployee_EmployeeCode(employeeCode)
                    .orElseThrow(() -> new DataNotFoundException("현재 계정의 메신저를 읽어올 수 없습니다."));
            Long messengerCode = messenger.getMessengerCode();
            Chatroom chatroom = new Chatroom()
                    .setMessengerCode(messengerCode)
                    .setChatroomTitle(chatroomOptionsDTO.getChatroomTitle());
            List<Employee> newEmployeeList = employeeRepository.findAllByEmployeeRetirementDateIsNullAndEmployeeCodeIn(chatroomOptionsDTO.getEmployeeCodeList());
            List<ChatroomMember> chatroomMemberList =
                    newEmployeeList.stream()
                            .map(employee ->
                                    new ChatroomMember()
                                            .setEmployee(employee)
                                            .setChatroomMemberType(employee.getEmployeeCode().equals(employeeCode) ? "관리자" : "일반사원")
                                            .setChatroomMemberInviteTime(LocalDateTime.now()))
                            .toList();
            chatroom = chatroom
                    .setChatroomMemberList(chatroomMemberList)
                    .setChatroomFixedStatus("N")
                    .builder();
            chatroomRepository.save(chatroom);
            result = 1;
        } catch (Exception e) {
            throw new DataInsertionException("채팅방 만들기에 실패했습니다.");
        }
        return result > 0 ? "채팅방 만들기 성공" : "채팅방 만들기 실패";
    }

    /*
     * 채팅방 들어가기
     * */
    public void selectChatroom(Long chatroomCode, Long employeeCode) {
        Messenger messenger = messengerRepository.findByEmployee_EmployeeCode(employeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정의 메신저를 읽어올 수 없습니다."));
        Chatroom chatroom = chatroomRepository.findById(chatroomCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방 정보를 읽어올 수 없습니다."));
        if (messenger.getChatroomList().contains(chatroom)) {
            // 맞는거니깐 채팅방에 대한 정보를 반환
        }
        // 아니면 오류를 반환
    }

    /*
     * 채팅 검색하기
     * 검색버튼을 누르면 데이터를 읽어오는 방식.
     * */
    public void searchChatList(String searchValue, Long chatroomCode, Long employeeCode) {
        Messenger messenger = messengerRepository.findByEmployee_EmployeeCode(employeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정의 메신저를 읽어올 수 없습니다."));
        Chatroom chatroom = chatroomRepository.findById(chatroomCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방 정보를 읽어올 수 없습니다."));
        if (messenger.getChatroomList().contains(chatroom)) {
            // 맞는거니깐 본격적으로 검색 시작.
            List<Chat> chatList = chatRepository
                    .findAllByChatroomCodeAndChatContentLike(chatroom.getChatroomCode(), "%" + searchValue + "%");
        }
        // 아니면 오류를 반환
    }

    /*
     * 사진 모아보기도 있어야 겠는데
     * */
    public void selectFileList(Long chatroomCode, Long employeeCode) {
        Messenger messenger = messengerRepository.findByEmployee_EmployeeCode(employeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정의 메신저를 읽어올 수 없습니다."));
        Chatroom chatroom = chatroomRepository.findById(chatroomCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방 정보를 읽어올 수 없습니다."));
        if (messenger.getChatroomList().contains(chatroom)) {
            // 맞는거니깐 본격적으로 검색 시작.
            List<Chat> chatList = chatRepository.findAllByChatroomCode(chatroomCode);
            List<List<ChatFile>> chatFileList = chatList.stream().map(chat -> chat.getChatFileList())
                    .toList();
        }
        // 아니면 오류를 반환
    }

    /*
     * 채팅방 초대하기
     * */
    @Transactional
    public void inviteChatroomMembers(List<Long> employeeCodeList, Long chatroomCode, Long employeeCode) {
        ChatroomMember adminChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, employeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정은 해당 채팅방의 관리자가 아닙니다."));

        List<Long> chatroomMemberEmployeeCodeList = chatroomMemberRepository.findAllByChatroomCode(chatroomCode)
                .stream().map(chatroomMember -> chatroomMember.getEmployee().getEmployeeCode())
                .toList();
        employeeCodeList.removeAll(chatroomMemberEmployeeCodeList);
        List<Employee> employeeList = employeeRepository.findAllByEmployeeRetirementDateIsNullAndEmployeeCodeIn(employeeCodeList);
        LocalDateTime now = LocalDateTime.now();
        employeeList.stream().map(employee -> new ChatroomMember()
                        .setChatroomCode(chatroomCode)
                        .setEmployee(employee)
                        .setChatroomMemberType("일반사원")
                        .setChatroomMemberInviteTime(now))
                .forEach(chatroomMember -> chatroomMemberRepository.save(chatroomMember));
    }

    /*
     * 채팅방에서 내보내기
     * */
    @Transactional
    public void kickChatroomMembers(Long kickedChatroomMemberCode, Long chatroomCode, Long employeeCode) {
        ChatroomMember adminChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, employeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정은 해당 채팅방의 관리자가 아닙니다."));

        ChatroomMember kickedChatroomMember = chatroomMemberRepository.findByChatroomMemberCodeAndChatroomCode(kickedChatroomMemberCode, chatroomCode)
                .orElseThrow(() -> new DataNotFoundException("강퇴할 계정의 정보가 없습니다."));

        kickedChatroomMember.setChatroomMemberType("퇴장사원");
    }
    /*
     * 채팅방 수정하기
     * */
    @Transactional
    public void modifyChatroomOptions(ChatroomOptions, Long chatroomCode, Long employeeCode) {
        ChatroomMember adminChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, employeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정은 해당 채팅방의 관리자가 아닙니다."));

        ChatroomMember kickedChatroomMember = chatroomMemberRepository.findByChatroomMemberCodeAndChatroomCode(kickedChatroomMemberCode, chatroomCode)
                .orElseThrow(() -> new DataNotFoundException("강퇴할 계정의 정보가 없습니다."));

        kickedChatroomMember.setChatroomMemberType("퇴장사원");
    }

    /*
     * 채팅방 프로필사진 수정하기
     * */

    /*
     * 채팅방 나가기(남은 인원수를 가져와서 여러명이면 내 상태만 바뀌고, 나혼자 남으면 채팅방 자체가 사라져야 한다.)
     * */
}
