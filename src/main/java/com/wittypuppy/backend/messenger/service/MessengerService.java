package com.wittypuppy.backend.messenger.service;

import com.wittypuppy.backend.common.dto.Criteria;
import com.wittypuppy.backend.common.exception.DataDeletionException;
import com.wittypuppy.backend.common.exception.DataInsertionException;
import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.common.exception.DataUpdateException;
import com.wittypuppy.backend.messenger.dto.*;
import com.wittypuppy.backend.messenger.entity.*;
import com.wittypuppy.backend.messenger.repository.*;
import com.wittypuppy.backend.util.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MessengerService {
    private final ChatReadStatusRepository chatReadStatusRepository;
    private final ChatRepository chatRepository;
    private final ChatroomMemberRepository chatroomMemberRepository;
    private final ChatroomProfileRepository chatroomProfileRepository;
    private final ChatroomRepository chatroomRepository;
    private final EmployeeRepository employeeRepository;
    private final MessengerRepository messengerRepository;
    private final ModelMapper modelMapper;

    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    /**
     * 내가 속한 채팅방의 채팅방 식별 코드에 대한 리스트를 가져온다. 채팅방 코드와 내 사원 코드를 이용해서 채팅 엔티티의 최근 채팅코드(max), 채팅읽음여부 엔티티의 채팅코드를 읽어온다.
     * <p>
     * 만약 두 채팅에 차이가 발생하면. (일반적으로 채팅 엔티티의 채팅코드가 작거나 같은데. 이때 작을 경우를 말한다.) 즉시 메시지가 있다고 표시하고 반환한다.
     * <p>
     * 모든 쿼리문이 끝나고 나서 두 채팅코드에 차이가 없다면 안읽은 메시지가 없다고 하고 반환한다.
     *
     * @param userEmployeeCode
     * @return 구독을 위한 Chatroom 엔티티의 식별 코드 리스트와 남은 메시지 있는지에 대한 여부를 DTO로 반환
     */
    public ChatroomInfo selectChatroomList(Long userEmployeeCode) {
        ChatroomInfo chatroomInfo = new ChatroomInfo();

        List<Chatroom> chatroomList = chatroomRepository.findAllChatroomByEmployeeCodeAndDeleteStatus(userEmployeeCode);
        List<Long> chatroomCodeList = chatroomList.stream().map(chatroom -> chatroom.getChatroomCode()).toList();
        chatroomInfo.setChatroomCodeList(chatroomCodeList);
        for (Long chatroomCode : chatroomCodeList) {
            RecentChatInterface recentChatInterface = chatRepository.findChatCodesInChatAndChatReadStatus(chatroomCode, userEmployeeCode);
            chatroomInfo.setIsRemainingChat("N");
            if ((recentChatInterface.getChatCode() != null && recentChatInterface.getUserChatCode() == null)
                    || (recentChatInterface.getUserChatCode() != null && (recentChatInterface.getChatCode() > recentChatInterface.getUserChatCode()))) {
                chatroomInfo.setIsRemainingChat("Y");
                break;
            }
        }
        return chatroomInfo;
    }

    @Transactional
    public MessengerMainDTO openMessenger(Long userEmployeeCode) {
        try {
            Messenger messenger = messengerRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                    .orElseGet(() -> {
                        Employee employee = employeeRepository.findById(userEmployeeCode)
                                .orElseThrow(() -> new DataNotFoundException("사원 정보가 없습니다."));
                        Messenger newMessenger = new Messenger()
                                .setEmployee(employee)
                                .setMessengerOption("RD")
                                .setMessengerMiniAlarmOption("N")
                                .setMessengerTheme("DEFAULT")
                                .builder();
                        messengerRepository.save(newMessenger);
                        return newMessenger;
                    });
            MessengerMainDTO messengerMainDTO = new MessengerMainDTO()
                    .setMessengerCode(messenger.getMessengerCode())
                    .setMessengerPositionOption(messenger.getMessengerOption())
                    .setMessengerMiniAlarmOption(messenger.getMessengerMiniAlarmOption())
                    .setMessengerTheme(messenger.getMessengerTheme())
                    .builder();

            List<ChatroomMessengerMainInterface> chatroomMessengerMainDTOList = messengerRepository.getMessengerStatistics(userEmployeeCode);
            messengerMainDTO.setChatroomList(chatroomMessengerMainDTOList);

            return messengerMainDTO;
        } catch (Exception e) {
            throw new DataInsertionException("메신저 정보가 존재하지 않아 추가하려 했으나 실패했습니다.");
        }
    }

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

    @Transactional
    public MessengerOptionsDTO modifyMessengerOptions(MessengerOptionsDTO messengerOptionsDTO, Long userEmployeeCode) {
        Messenger messenger = messengerRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정의 메신저 정보가 없습니다."));
        try {
            messenger = messenger
                    .setMessengerOption(messengerOptionsDTO.getMessengerPositionOption())
                    .setMessengerMiniAlarmOption(messengerOptionsDTO.getMessengerMiniAlarmOption())
                    .setMessengerTheme(messengerOptionsDTO.getMessengerTheme())
                    .builder();
            messengerRepository.save(messenger);
            return messengerOptionsDTO;
        } catch (Exception e) {
            throw new DataUpdateException("메신저 옵션 수정 실패");
        }
    }

    @Transactional
    public Long pinnedChatroom(Long chatroomCode, Long userEmployeeCode) {
        try {
            ChatroomMember chatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployeeCodeAndIsNotDelete(chatroomCode, userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("현재 선택한 채팅방이 존재하지 않습니다."));
            if (chatroomMember.getChatroomMemberPinnedStatus().equals("N")) {
                chatroomMember = chatroomMember
                        .setChatroomMemberPinnedStatus("Y")
                        .builder();
            } else {
                chatroomMember = chatroomMember
                        .setChatroomMemberPinnedStatus("N")
                        .builder();
            }
            chatroomMemberRepository.save(chatroomMember);
            return chatroomCode;
        } catch (Exception e) {
            throw new DataUpdateException("메신저 고정/고정해제 실패");
        }
    }

    @Transactional
    public Long createChatroom(ChatroomOptionsDTO chatroomOptionsDTO, Long userEmployeeCode) {
        try {
            String chatroomTitle = chatroomOptionsDTO.getChatroomTitle();
            LocalDateTime now = LocalDateTime.now();
            Employee employee = employeeRepository.findByEmployeeCodeAndEmployeeRetirementDateIsNull(userEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("현재 계정 정보가 존재하지 않습니다."));
            ChatroomMember chatroomMember = new ChatroomMember()
                    .setEmployee(employee)
                    .setChatroomMemberType("관리자")
                    .setChatroomMemberInviteTime(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                    .setChatroomMemberPinnedStatus(chatroomOptionsDTO.getChatroomFixedStatus())
                    .builder();
            ChatroomProfile chatroomProfile = new ChatroomProfile()
                    .setChatroomProfileRegistDate(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                    .setChatroomProfileDeleteStatus("N")
                    .builder();
            ChatReadStatus chatReadStatus = new ChatReadStatus()
                    .builder();
            Chatroom chatroom = new Chatroom()
                    .setChatroomTitle(chatroomTitle)
                    .setChatroomMemberList(Collections.singletonList(chatroomMember))
                    .setChatroomProfileList(Collections.singletonList(chatroomProfile))
                    .setChatReadStatusList(Collections.singletonList(chatReadStatus))
                    .builder();
            chatroomRepository.save(chatroom);

            chatReadStatus = chatReadStatus.setChatroomMemberCode(chatroomMember.getChatroomMemberCode())
                    .builder();
            chatReadStatusRepository.save(chatReadStatus);
            return chatroom.getChatroomCode();
        } catch (Exception e) {
            throw new DataInsertionException("채팅방 생성 실패");
        }
    }

    public ChatroomMainDTO openChatroom(Long chatroomCode, Long userEmployeeCode) {
        ChatroomMainDTO chatroomMainDTO = new ChatroomMainDTO();
        chatroomMainElseChatInterface chatroomMainElseChat = chatroomRepository.findByChatroomCodeAndEmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("채팅방 정보를 찾을 수 없습니다."));
        String chatroomTitle = chatroomMainElseChat.getChatroomTitle();
        String chatroomProfileFileURL = chatroomMainElseChat.getChatroomProfileFileURL();
        Long lastReadChatCode = chatroomMainElseChat.getLastReadChatCode();
        String pinnedStatus = chatroomMainElseChat.getPinnedStatus();
        List<ChatroomMember> chatroomMemberList = chatroomMemberRepository.findAllByChatroomCodeAndChatroomMemberTypeIsNot(chatroomCode, "삭제");
        List<ChatroomMemberDTO> chatroomMemberDTOList = chatroomMemberList.stream().map(chatroomMember -> modelMapper.map(chatroomMember, ChatroomMemberDTO.class))
                .collect(Collectors.toList());
        List<Chat> chatList = null;

        if (lastReadChatCode == null) {
            chatList = chatRepository.findAllByChatroomCode(chatroomCode);
        } else {
            chatList = chatRepository.findAllByChatroomCodeAndChatCodeIsGreaterThan(chatroomCode, lastReadChatCode);
            PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "chatCode"));
            List<Chat> nextChatList = chatRepository.findAllByChatroomCodeAndChatCodeIsLessThanEqual(chatroomCode, lastReadChatCode, pageRequest);
            Collections.reverse(nextChatList);
            chatList = Stream.concat(nextChatList.stream(), chatList.stream())
                    .collect(Collectors.toList());
        }
        List<ChatDTO> chatDTOList = chatList.stream().map(chat -> modelMapper.map(chat, ChatDTO.class))
                .collect(Collectors.toList());
        chatroomMainDTO = chatroomMainDTO.setChatroomCode(chatroomMainElseChat.getChatroomCode())
                .setChatroomTitle(chatroomTitle)
                .setChatroomProfileFileURL(chatroomProfileFileURL)
                .setLastReadChatCode(lastReadChatCode)
                .setPinnedStatus(pinnedStatus)
                .setChatroomMemberDTOList(chatroomMemberDTOList)
                .setChatDTOList(chatDTOList)
                .builder();

        return chatroomMainDTO;
    }

    public List<ChatDTO> selectChatListWithScrollPaging(Long chatroomCode, Long requestChatCode, Long userEmployeeCode) {
        List<ChatDTO> chatDTOList = new ArrayList<>(Collections.singletonList(new ChatDTO()));
        if (requestChatCode == null) {
            return chatDTOList;
        }

        PageRequest pageRequest = PageRequest.of(0, 9, Sort.by(Sort.Direction.DESC, "chatCode"));
        List<Chat> prevChatList = chatRepository.findAllByChatroomCodeAndChatCodeIsLessThan(chatroomCode, requestChatCode, pageRequest);
        Collections.reverse(prevChatList);

        chatDTOList = prevChatList.stream().map(chat -> modelMapper.map(chat, ChatDTO.class))
                .collect(Collectors.toList());
        return chatDTOList;
    }

    public ChatroomOptionsDTO openChatroomOptions(Long chatroomCode, Long userEmployeeCode) {
        Chatroom chatroom = chatroomRepository.findByChatroomCodeAndChatroomMemberList_Employee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
        ChatroomMember chatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCodeAndChatroomMemberTypeNot(chatroomCode, userEmployeeCode, "삭제")
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
        ChatroomOptionsDTO chatroomOptionsDTO = new ChatroomOptionsDTO()
                .setChatroomTitle(chatroom.getChatroomTitle())
                .setChatroomFixedStatus(chatroomMember.getChatroomMemberPinnedStatus())
                .builder();
        return chatroomOptionsDTO;
    }

    @Transactional
    public String modifyChatroomOptions(Long chatroomCode, ChatroomOptionsDTO chatroomOptionsDTO, Long userEmployeeCode) {
        String chatroomTitle = chatroomOptionsDTO.getChatroomTitle();
        String chatroomFixedStatus = chatroomOptionsDTO.getChatroomFixedStatus();
        Chatroom chatroom = chatroomRepository.findByChatroomCodeAndChatroomMemberList_Employee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
        ChatroomMember chatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCodeAndChatroomMemberTypeNot(chatroomCode, userEmployeeCode, "삭제")
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
        chatroomMember = chatroomMember.setChatroomMemberPinnedStatus(chatroomFixedStatus);
        if (chatroomMember.getChatroomMemberType().equals("관리자")) {
            chatroom = chatroom.setChatroomTitle(chatroomTitle).builder();
        } else {
            return "해당 계정은 채팅방 관리자가 아니어서 채팅방 고정여부만 변경됩니다.";
        }
        return "채팅방 옵션 변경 성공";
    }

    public List<EmployeeDTO> selectEmployees() {
        List<Employee> employeeList = employeeRepository.findAllByEmployeeRetirementDateIsNull();
        List<EmployeeDTO> employeeDTOList = employeeList.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .toList();
        return employeeDTOList;
    }

    @Transactional
    public List<ChatroomMemberDTO> inviteEmployee(Long chatroomCode, Long inviteEmployeeCode, Long userEmployeeCode) {
        ChatroomMember userChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCodeAndChatroomMemberTypeNot(chatroomCode, userEmployeeCode, "삭제")
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 계정 정보가 없습니다."));
        try {
            LocalDateTime now = LocalDateTime.now();
            Employee employee = employeeRepository.findById(inviteEmployeeCode)
                    .orElseThrow(() -> new DataNotFoundException("초대할 사원이 존재하지 않습니다."));

            ChatroomMember newChatroomMember = new ChatroomMember()
                    .setChatroomCode(chatroomCode)
                    .setChatroomMemberPinnedStatus("N")
                    .setEmployee(employee)
                    .setChatroomMemberType("일반사원")
                    .setChatroomMemberInviteTime(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                    .builder();
            chatroomMemberRepository.save(newChatroomMember);

            ChatReadStatus newChatReadStatus = new ChatReadStatus()
                    .setChatroomCode(chatroomCode)
                    .setChatroomMemberCode(newChatroomMember.getChatroomMemberCode())
                    .builder();
            chatReadStatusRepository.save(newChatReadStatus);

            List<ChatroomMember> chatroomMemberList = chatroomMemberRepository.findAllByChatroomCodeAndChatroomMemberTypeIsNot(chatroomCode, "삭제");
            List<ChatroomMemberDTO> chatroomMemberDTOList = chatroomMemberList.stream().map(chatroomMember -> modelMapper.map(chatroomMember, ChatroomMemberDTO.class))
                    .collect(Collectors.toList());
            return chatroomMemberDTOList;
        } catch (Exception e) {
            throw new DataUpdateException("채팅방 초대 실패");
        }
    }

    public List<ChatroomMemberDTO> selectChatroomMember(Long chatroomCode, Long userEmployeeCode) {
        List<ChatroomMember> existChatroomMemberList = chatroomMemberRepository.findAllByChatroomCodeAndChatroomMemberTypeIn(chatroomCode, List.of("관리자", "일반사원"));
        List<ChatroomMemberDTO> chatroomMemberDTOList = existChatroomMemberList.stream().map(chatroomMember -> modelMapper.map(chatroomMember, ChatroomMemberDTO.class))
                .collect(Collectors.toList());
        return chatroomMemberDTOList;
    }

    @Transactional
    public String delegateChatroomAdmin(Long delegateChatroomMemberCode, Long chatroomCode, Long userEmployeeCode) {
        ChatroomMember userChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCodeAndChatroomMemberTypeNot(chatroomCode, userEmployeeCode, "삭제")
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 로그인한 계정 정보가 없습니다."));
        if (!userChatroomMember.getChatroomMemberType().equals("관리자")) {
            return "관리자 계정이 아닙니다.";
        }
        ChatroomMember delegateChatroomMember = chatroomMemberRepository.findByChatroomMemberCodeAndChatroomCode(chatroomCode, delegateChatroomMemberCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 관리자를 위임할 계정 정보가 없습니다."));
        if (!delegateChatroomMember.getChatroomMemberType().equals("일반사원")) {
            return "해당 계정은 위임할 수 없습니다.";
        }
        delegateChatroomMember = delegateChatroomMember.setChatroomMemberType("관리자").builder();
        userChatroomMember = userChatroomMember.setChatroomMemberType("일반사원").builder();
        chatroomMemberRepository.save(delegateChatroomMember);
        chatroomMemberRepository.save(userChatroomMember);
        return "관리자 위임 성공";
    }

    public String findProfileImage(Long chatroomCode, Long userEmployeeCode) {
        ChatroomProfile chatroomProfile = chatroomProfileRepository.findFirstByChatroomCodeOrderByChatroomProfileRegistDateDesc(chatroomCode)
                .orElseThrow(() -> new DataNotFoundException("프로필 사진이 존재하지 않습니다."));

        return chatroomProfile.getChatroomProfileChangedFile();
    }

    @Transactional
    public String updateProfileImage(Long chatroomCode, MultipartFile chatroomProfileImage, Long userEmployeeCode) {
        try {
            LocalDateTime now = LocalDateTime.now();
            ChatroomProfile chatroomProfile = chatroomProfileRepository.findFirstByChatroomCodeOrderByChatroomProfileRegistDateDesc(chatroomCode)
                    .orElseGet(() -> {
                        ChatroomProfile tempChatroomProfile = new ChatroomProfile()
                                .setChatroomCode(chatroomCode)
                                .setChatroomProfileRegistDate(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                                .setChatroomProfileDeleteStatus("N")
                                .builder();
                        chatroomProfileRepository.save(tempChatroomProfile);
                        return tempChatroomProfile;
                    });

            String imageName = UUID.randomUUID().toString().replace("-", "");
            String replaceFileName = null;
            try {
                replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, chatroomProfileImage);
                chatroomProfile = chatroomProfile.setChatroomProfileOgFile(imageName)
                        .setChatroomProfileChangedFile(replaceFileName)
                        .builder();
                chatroomProfileRepository.save(chatroomProfile);
            } catch (IOException e) {
                FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
                throw new DataUpdateException("프로필 사진 변경 실패");
            }
        } catch (Exception e) {
            throw new DataUpdateException("프로필 사진 변경 실패");
        }
        return "프로필 사진 변경 성공";
    }

    @Transactional
    public String kickChatroomMember(Long kickChatroomMemberCode, Long chatroomCode, Long userEmployeeCode) {
        ChatroomMember userChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCodeAndChatroomMemberTypeNot(chatroomCode, userEmployeeCode, "삭제")
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 로그인한 계정 정보가 없습니다."));
        if (!userChatroomMember.getChatroomMemberType().equals("관리자")) {
            return "현재 접속한 계정은 관리자가 아닙니다.";
        }
        ChatroomMember kickChatroomMember = chatroomMemberRepository.findByChatroomMemberCodeAndChatroomCode(chatroomCode, kickChatroomMemberCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 내보낼 사원의 계정 정보가 없습니다."));
        kickChatroomMember = kickChatroomMember
                .setChatroomMemberType("삭제")
                .builder();
        chatroomMemberRepository.save(kickChatroomMember);
        return "해당 채팅방 멤버를 내보냈습니다.";
    }

    @Transactional
    public Long leaveChatroomMember(Long chatroomCode, Long userEmployeeCode) {
        ChatroomMember userChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCodeAndChatroomMemberTypeNot(chatroomCode, userEmployeeCode, "삭제")
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 로그인한 계정 정보가 없습니다."));
        List<ChatroomMember> chatroomMemberList = chatroomMemberRepository.findAllByChatroomCodeAndChatroomMemberTypeIn(chatroomCode, List.of("일반사원", "관리자"));
        try {
            if (chatroomMemberList.size() <= 1) {
                Chatroom chatroom = chatroomRepository.findById(chatroomCode)
                        .orElseThrow(() -> new DataNotFoundException("채팅방 멤버가 한명만 남아서 채팅방을 삭제하려 했으나 채팅방 정보를 찾을 수 없습니다."));
                chatroomRepository.delete(chatroom);
            } else {
                userChatroomMember = userChatroomMember.setChatroomMemberType("삭제")
                        .builder();
                chatroomMemberRepository.save(userChatroomMember);
            }
            return chatroomCode;
        } catch (Exception e) {
            throw new DataDeletionException("채팅방 나가기 실패");
        }
    }

    @Transactional
    public ChatDTO sendChat(Long chatroomCode, SendDTO sendDTO) {
        try {
            Long emplyoeeCode = Long.valueOf(sendDTO.getEmployeeCode());
            LocalDateTime now = LocalDateTime.now();
            String chatContent = sendDTO.getChatContent();

            ChatroomMember chatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCodeAndChatroomMemberTypeNot(chatroomCode, emplyoeeCode, "삭제")
                    .orElseThrow(() -> new DataNotFoundException("현재 데이터베이스에 계정 정보가 없습니다."));

            Chat chat = new Chat()
                    .setChatroomCode(chatroomCode)
                    .setChatroomMember(chatroomMember)
                    .setChatWriteDate(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                    .setChatContent(chatContent)
                    .builder();
            chatRepository.save(chat);
            ChatDTO chatDTO = modelMapper.map(chat, ChatDTO.class);
            return chatDTO;
        } catch (Exception e) {
            throw new DataInsertionException("채팅메시지 전송 실패");
        }
    }

    public Long getEmployeeCode(Long chatroomMemberCode) {
        ChatroomMember chatroomMember = chatroomMemberRepository.findById(chatroomMemberCode)
                .orElseThrow(() -> new DataNotFoundException("잘못된 접근"));
        return chatroomMember.getEmployee().getEmployeeCode();
    }

    @Transactional
    public ChatroomMessengerMainInterface updateChatReadStatus(Long chatroomCode, Long userEmployeeCode) {
        try {
            ChatroomMember chatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCodeAndChatroomMemberTypeNot(chatroomCode, userEmployeeCode, "삭제")
                    .orElseThrow(() -> new DataNotFoundException("현재 계정이 채팅방 멤버 정보에 없습니다."));
            ChatReadStatus chatReadStatus = chatReadStatusRepository.findByChatroomCodeAndChatroomMemberCode(chatroomCode, chatroomMember.getChatroomMemberCode())
                    .orElseThrow(() -> new DataNotFoundException("현재 계정이 채팅방 멤버 정보에 없습니다."));
            Chat maxChatCode = chatRepository.findFirstByChatroomCodeOrderByChatCodeDesc(chatroomCode)
                    .orElse(null); //
            if (maxChatCode == null) {
                chatReadStatus.setChatCode(null);
            } else {
                chatReadStatus.setChatCode(maxChatCode.getChatCode());
            }
            ChatroomMessengerMainInterface chatroomMessengerMainInterface = messengerRepository.getMessengerStatisticByChatroomCode(userEmployeeCode, chatroomCode)
                    .orElseThrow(() -> new DataNotFoundException("메신저 메인화면에 갱신되는 정보를 가져올 수 없습니다."));
            return chatroomMessengerMainInterface;
        } catch (Exception e) {
            throw new DataUpdateException("채팅 관찰 시점에 대한 갱신에 실패");
        }
    }

    @Transactional
    public ChatroomMessengerMainInterface newChatroom(Long employeeCode, Long chatroomCode) {
        ChatroomMessengerMainInterface chatroomMessengerMainInterface = messengerRepository.getMessengerStatisticByChatroomCode(employeeCode, chatroomCode)
                .orElseThrow(() -> new DataNotFoundException("해당 채팅방 정보를 찾을 수 없습니다."));
        return chatroomMessengerMainInterface;
    }

    public Map<String, Object> findChatList(Long chatroomCode, String searchValue, Long userEmployeeCode, Criteria cri) {
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        PageRequest pageRequest = PageRequest.of(index, count);
        Page<Chat> chatList = chatRepository.findAllByChatroomCodeAndChatContentLike(chatroomCode, searchValue, pageRequest);
        List<ChatDTO> chatDTOList = chatList
                .getContent()
                .stream()
                .map(chat -> modelMapper.map(chat, ChatDTO.class))
                .collect(Collectors.toList());

        Map<String, Object> returnMap = new HashMap<>();

        returnMap.put("totalSize", chatList.getTotalElements());
        returnMap.put("content", chatDTOList);

        return returnMap;
    }
}