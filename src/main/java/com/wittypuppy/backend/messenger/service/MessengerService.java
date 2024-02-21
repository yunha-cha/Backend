package com.wittypuppy.backend.messenger.service;

import com.wittypuppy.backend.common.exception.DataInsertionException;
import com.wittypuppy.backend.common.exception.DataNotFoundException;
import com.wittypuppy.backend.common.exception.DataUpdateException;
import com.wittypuppy.backend.messenger.config.MessengerConfig;
import com.wittypuppy.backend.messenger.dto.*;
import com.wittypuppy.backend.messenger.entity.*;
import com.wittypuppy.backend.messenger.repository.*;
import com.wittypuppy.backend.util.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
    private final MessengerConfig messengerConfig;
    private final Map<Long, Set<Long>> oldChatMemberMap;
    private final Map<Long, Set<Long>> newChatMemberMap;

    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    @Value("${image.image-url}")
    private String IMAGE_URL;

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
        if (chatroomList.size() == 0) {
            // 만약 채팅방 목록이 없으면 읽어올 데이터도 없다는 뜻이다
            throw new DataNotFoundException("초기 채팅방 세팅을 하려 했으나 데이터가 없습니다.");
        }
        List<Long> chatroomCodeList = chatroomList.stream().map(chatroom -> chatroom.getChatroomCode()).toList();
        chatroomInfo.setChatroomCodeList(chatroomCodeList);

        for (Long chatroomCode : chatroomCodeList) {
            Long[] chatCode = chatRepository.findChatCodesInChatAndChatReadStatus(chatroomCode, userEmployeeCode);
            if (chatCode != null && chatCode[0] > chatCode[1]) {
                chatroomInfo.setIsRemainingChat("Y");
                return chatroomInfo;
            }
        }
        chatroomInfo.setIsRemainingChat("N");
        return chatroomInfo;
    }

    /* 메신저 열기 */
    @Transactional
    public MessengerMainDTO openMessenger(Long userEmployeeCode) {
        try {
            Messenger messenger = messengerRepository.findByEmployee_EmployeeCode(userEmployeeCode) // 사원 코드를 통해 메신저를 읽어온다. 없으면 생성
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
            MessengerMainDTO messengerMainDTO = new MessengerMainDTO()
                    .setMessengerCode(messenger.getMessengerCode())
                    .setMessengerOption(messenger.getMessengerOption())
                    .setMessengerMiniAlarmOption(messenger.getMessengerMiniAlarmOption())
                    .setMessengerTheme(messenger.getMessengerTheme())
                    .builder();
            // 채팅방이 조금이라도 있을 경우
            List<ChatroomMessengerMainDTO> chatroomMessengerMainDTOList = messengerRepository.getMessengerStatistics(userEmployeeCode);
            messengerMainDTO.setChatroomList(chatroomMessengerMainDTOList);

            return messengerMainDTO;
        } catch (Exception e) {
            throw new DataInsertionException("메신저 정보가 존재하지 않아 추가하려 했으나 실패했습니다.");
        }
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
    public String modifyMessengerOptions(MessengerOptionsDTO messengerOptionsDTO, Long userEmployeeCode) {
        Messenger messenger = messengerRepository.findByEmployee_EmployeeCode(userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정의 메신저 정보가 없습니다."));
        try {
            messenger = messenger
                    .setMessengerOption(messengerOptionsDTO.getMessengerPositionOption())
                    .setMessengerMiniAlarmOption(messengerOptionsDTO.getMessengerMiniAlarmOption())
                    .setMessengerTheme(messengerOptionsDTO.getMessengerTheme())
                    .builder();
            return "메신저 옵션 수정 성공";
        } catch (Exception e) {
            throw new DataUpdateException("메신저 옵션 수정 실패");
        }
    }

    /* 채팅방 고정하기 */
    @Transactional
    public String pinnedChatroom(Long chatroomCode, Long userEmployeeCode) {
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
            return "메신저 고정/고정해제 성공";
        } catch (Exception e) {
            throw new DataUpdateException("메신저 고정/고정해제 실패");
        }
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
        try {
            String chatroomTitle = chatroomOptionsDTO.getChatroomTitle();
            LocalDateTime now = LocalDateTime.now();
            Employee employee = employeeRepository.findByEmployeeCodeAndEmployeeRetirementDateIsNull(userEmployeeCode)
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
                    .setChatroomMemberList(Collections.singletonList(chatroomMember))
                    .setChatroomProfileList(Collections.singletonList(chatroomProfile))
                    .setChatReadStatusList(Collections.singletonList(chatReadStatus))
                    .builder();
            chatroomRepository.save(chatroom);
            return "채팅방 생성 성공";
        } catch (Exception e) {
            throw new DataInsertionException("채팅방 생성 실패");
        }
    }

    /* 채팅방 접속하기 */
    public ChatroomMainDTO openChatroom(Long chatroomCode, Long userEmployeeCode) {
        ChatroomMainDTO chatroomMainDTO = new ChatroomMainDTO();
        Object[] chatroomMainElseChat = chatroomRepository.findByChatroomCodeAndEmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("채팅방 정보를 찾을 수 없습니다."));
        String chatroomTitle = (String) chatroomMainElseChat[1];
        String chatroomProfileFileURL = (String) chatroomMainElseChat[2];
        Long lastReadChatCode = (Long) chatroomMainElseChat[3];
        String pinnedStatus = (String) chatroomMainElseChat[4];
        List<ChatroomMember> chatroomMemberList = chatroomMemberRepository.findAllByChatroomCodeAndChatroomMemberTypeIsNot(chatroomCode, "삭제");
        List<ChatroomMemberDTO> chatroomMemberDTOList = chatroomMemberList.stream().map(chatroomMember -> modelMapper.map(chatroomMember, ChatroomMemberDTO.class))
                .collect(Collectors.toList());
        /*
         * 1. 완전 처음 접속하면 처음 페이지를 보여줘야 된다.
         * 2. 중간에 접속하면 보지못한 페이지부터 보여줘야 한다. (여기서 보여주는거는 현재페이지 + 이전페이지 + 다음페이지. 세가지를 읽고 묶어서 전달)
         * */
        Long chatCount = chatRepository.getChatCount(chatroomCode); // 현재 채팅방 전체 채팅 개수
        Long myChatCount = 0L;
        if (lastReadChatCode != null) {
            myChatCount = chatRepository.getMyChatCount(chatroomCode, lastReadChatCode); // 내가 있어야 하는 채팅 부분
        }
        // 그러면 이제 페이지가 얼추 계산된다.
        Pageable pageable = null;
        Long pageNumLong = 0L;
        int pageSize = 50;
        List<Chat> chatList = null;
        if (lastReadChatCode == null) { // 처음 들어왔으면 맨 처음 페이지
            pageable = PageRequest.of(0, pageSize);
            chatList = chatRepository.selectChatListByChatroomCodeWithPaging(chatroomCode, pageable);
        } else { // 여기는 페이징 작업
            pageNumLong = myChatCount == 0L ? 0L : (myChatCount - 1L) / pageSize;
            pageable = PageRequest.of(Math.toIntExact(pageNumLong), pageSize);
            chatList = chatRepository.selectChatListByChatroomCodeWithPaging(chatroomCode, pageable);
            if (pageNumLong != 0) {
                // 0페이지가 아닌 경우는 이전 페이지도 읽어서 반영한다. 가끔 단 1개만 읽어오는 경우가 발생할 수 있다.
                pageable = PageRequest.of(Math.toIntExact(pageNumLong - 1L), pageSize);
                List<Chat> beforeChatList = chatRepository.selectChatListByChatroomCodeWithPaging(chatroomCode, pageable);
                beforeChatList.addAll(chatList);
                chatList = beforeChatList;
            }
        }
        /*맨 마지막의 페이지 번호*/
        Long recentPageNum = chatCount == 0L ? 0L : (chatCount - 1L) / pageSize;
        /*채팅 코드*/
        Long recentChatCode = chatRepository.findFirstByChatroomCodeOrderByChatCodeDesc(chatroomCode)
                .orElseThrow(() -> new DataNotFoundException("현재 마지막 채팅을 찾을 수 없습니다."))
                .getChatCode();
        /*그 채팅이 몇번째 데이터인지.*/
        Long recentPageChatCount = chatCount / pageSize;

        List<ChatDTO> chatDTOList = chatList.stream().map(chat -> modelMapper.map(chat, ChatDTO.class))
                .collect(Collectors.toList());
        chatroomMainDTO = chatroomMainDTO.setChatroomCode((Long) chatroomMainElseChat[0])
                .setChatroomTitle(chatroomTitle)
                .setChatroomProfileFileURL(chatroomProfileFileURL)
                .setLastReadChatCode(lastReadChatCode)
                .setPinnedStatus(pinnedStatus)
                .setChatroomMemberDTOList(chatroomMemberDTOList)
                .setChatDTOList(chatDTOList)
                .builder();

        return chatroomMainDTO;
    }

    public List<ChatDTO> selectChatListWithScrollPaging(Long chatroomCode, Long requestChatCode, String direction) {
        /*
         * 1. direction이 UP 인 경우
         *      request한 ChatCode 가 어느번째인지 찾는다.
         *      1페이지에 있다면. 굳이 시도하지 않는다.
         *      나머지 페이지에 있다면 시도해서 반환한다.
         * 2. direction이 DOWN 인 경우
         *     request한 ChatCode 가 어느번째인지 찾는다
         *     마지막 페이지에 있다면 굳이 시도하지 않는다.
         *     나머지 페이지에 있다면 시도해서 반환한다.
         * */
        Long chatCount = chatRepository.getChatCount(chatroomCode); // 현재 채팅방 전체 채팅 개수
        Long myRequestChatCount = chatRepository.getMyChatCount(chatroomCode, requestChatCode); // 내가 있는 채팅의 번째.

        List<Chat> chatList = null;
        Pageable pageable = null;
        Long pageNumLong = 0L;
        int pageSize = 50;
        Long lastPageNum = chatCount == 0L ? 0L : (chatCount - 1L) / pageSize;
        Long myRequestChatPageNum = myRequestChatCount == 0L ? 0L : (myRequestChatCount - 1L) / pageSize;
        if (direction.equals("UP")) {
            if (myRequestChatPageNum > 0L) {
                pageable = PageRequest.of((int) (myRequestChatPageNum - 1), pageSize);
                chatList = chatRepository.selectChatListByChatroomCodeWithPaging(chatroomCode, pageable);
            } else {
                // 애초에 request 했으면 안됐지만 예외 발생
                throw new DataNotFoundException("잘못된 데이터 요청입니다.");
            }
        } else {
            if (myRequestChatPageNum < lastPageNum) {
                pageable = PageRequest.of((int) (myRequestChatPageNum + 1), pageSize);
                chatList = chatRepository.selectChatListByChatroomCodeWithPaging(chatroomCode, pageable);
            } else {
                // 애초에 request 했으면 안됐지만 예외 발생. 왜냐하면 채팅에 들어올때 당연히 최근 채팅을 볼 것이므로.
                throw new DataNotFoundException("잘못된 데이터 요청입니다.");
            }
        }
        List<ChatDTO> chatDTOList = chatList.stream().map(chat -> modelMapper.map(chat, ChatDTO.class))
                .collect(Collectors.toList());
        return chatDTOList;
    }

    /* 채팅방 옵션창 열기 */
    public ChatroomOptionsDTO openChatroomOptions(Long chatroomCode, Long userEmployeeCode) {
        Chatroom chatroom = chatroomRepository.findByChatroomCodeAndChatroomMemberList_Employee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
        ChatroomMember chatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 이 채팅방의 멤버가 아닙니다."));
        ChatroomOptionsDTO chatroomOptionsDTO = new ChatroomOptionsDTO()
                .setChatroomTitle(chatroom.getChatroomTitle())
                .setChatroomFixedStatus(chatroomMember.getChatroomMemberPinnedStatus())
                .builder();
        return chatroomOptionsDTO;
    }

    /* 채팅방 옵션 저장하기 */
    @Transactional
    public String modifyChatroomOptions(Long chatroomCode, ChatroomOptionsDTO chatroomOptionsDTO, Long userEmployeeCode) {
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
            return "해당 계정은 채팅방 관리자가 아니어서 채팅방 고정여부만 변경됩니다.";
        }
        return "채팅방 옵션 변경 성공";
    }

    /* 전체 사원 목록 출력하기 */
    public List<EmployeeDTO> selectEmployees() {
        List<Employee> employeeList = employeeRepository.findAllByEmployeeRetirementDateIsNull();
        List<EmployeeDTO> employeeDTOList = employeeList.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .toList();
        return employeeDTOList;
    }

    /* 채팅방 초대하기 */
    @Transactional
    public String inviteEmployees(Long chatroomCode, List<Long> inviteEmployeeCodeList, Long userEmployeeCode) {
        ChatroomMember userChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 계정 정보가 없습니다."));
        if (!userChatroomMember.getChatroomMemberType().equals("관리자")) {
            return "해당 계정은 관리자가 아닙니다";
        }
        try {
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

                ChatReadStatus newChatReadStatus = new ChatReadStatus()
                        .setChatroomCode(chatroomCode)
                        .setChatroomMemberCode(newChatroomMember.getChatroomMemberCode())
                        .builder();
                chatReadStatusRepository.save(newChatReadStatus);

                newChatMemberMap.get(chatroomCode).add(newChatroomMember.getChatroomMemberCode());
            });
        } catch (Exception e) {
            throw new DataUpdateException("채팅방 초대 실패");
        }
        return "채팅방 초대 성공";
    }

    /*현재 채팅방에 있는 사람 조회*/
    public List<ChatroomMemberDTO> selectChatroomMember(Long chatroomCode, Long userEmployeeCode) {
        List<ChatroomMember> existChatroomMemberList = chatroomMemberRepository.findAllByChatroomCodeAndChatroomMemberTypeIn(chatroomCode, List.of("관리자", "일반사원"));
        List<ChatroomMemberDTO> chatroomMemberDTOList = existChatroomMemberList.stream().map(chatroomMember -> modelMapper.map(chatroomMember, ChatroomMemberDTO.class))
                .collect(Collectors.toList());
        return chatroomMemberDTOList;
    }

    /* 관리자 위임하기 */
    @Transactional
    public String delegateChatroomAdmin(Long delegateChatroomMemberCode, Long chatroomCode, Long userEmployeeCode) {
        ChatroomMember userChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
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
        return "관리자 위임 성공";
    }

    /* 채팅방 프로필 사진 가져오기 */
    public String findProfileImage(Long chatroomCode, Long userEmployeeCode) {
        ChatroomProfile chatroomProfile = chatroomProfileRepository.findFirstByChatroomCodeOrderByChatroomProfileRegistDateDesc(chatroomCode)
                .orElseThrow(() -> new DataNotFoundException("프로필 사진이 존재하지 않습니다."));


        return chatroomProfile.getChatroomProfileChangedFile();
    }

    /* 채팅방 프로필 사진 변경하기 */
    @Transactional
    public String updateProfileImage(Long chatroomCode, MultipartFile chatroomProfileImage, Long userEmployeeCode) {
        try {
            LocalDateTime now = LocalDateTime.now();
            ChatroomProfile chatroomProfile = chatroomProfileRepository.findFirstByChatroomCodeOrderByChatroomProfileRegistDateDesc(chatroomCode)
                    .orElseGet(() -> {
                        ChatroomProfile tempChatroomProfile = new ChatroomProfile()
                                .setChatroomProfileRegistDate(now)
                                .setChatroomProfileDeleteStatus("N")
                                .builder();
                        chatroomProfileRepository.save(tempChatroomProfile);
                        return tempChatroomProfile;
                    });
            chatroomProfile = chatroomProfile.setChatroomProfileDeleteStatus("Y");

            String imageName = UUID.randomUUID().toString().replace("-", "");
            String replaceFileName = null;
            try {
                replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, chatroomProfileImage);
                ChatroomProfile newChatroomProfile = new ChatroomProfile()
                        .setChatroomCode(chatroomCode)
                        .setChatroomProfileOgFile(chatroomProfileImage.getOriginalFilename())
                        .setChatroomProfileChangedFile(replaceFileName)
                        .setChatroomProfileRegistDate(now)
                        .setChatroomProfileDeleteStatus("N")
                        .builder();

            } catch (IOException e) {
                FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
                throw new DataUpdateException("프로필 사진 변경 실패");
            }
        } catch (Exception e) {
            throw new DataUpdateException("프로필 사진 변경 실패");
        }
        return "프로필 사진 변경 성공";
    }

    /* 채팅방 내보내기 */
    @Transactional
    public String kickChatroomMember(Long kickChatroomMemberCode, Long chatroomCode, Long userEmployeeCode) {
        ChatroomMember userChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
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
        oldChatMemberMap.get(chatroomCode).remove(kickChatroomMemberCode);
        return "해당 채팅방 멤버를 내보냈습니다.";
    }

    /* 채팅방 나가기 */
    @Transactional
    public String exitChatroomMember(Long chatroomCode, Long userEmployeeCode) {
        ChatroomMember userChatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 채팅방에 로그인한 계정 정보가 없습니다."));
        List<ChatroomMember> chatroomMemberList = chatroomMemberRepository.findAllByChatroomCodeAndChatroomMemberTypeIn(chatroomCode, List.of("일반사원", "관리자"));
        if (chatroomMemberList.size() <= 1) {
            Chatroom chatroom = chatroomRepository.findById(chatroomCode)
                    .orElseThrow(() -> new DataNotFoundException("채팅방 멤버가 한명만 남아서 채팅방을 삭제하려 했으나 채팅방 정보를 찾을 수 없습니다."));
            chatroomRepository.delete(chatroom);
            oldChatMemberMap.remove(chatroomCode);
            return "마지막에 채팅방을 나갔으므로 해당 채팅방이 삭제되었습니다.";
        } else {
            if (!userChatroomMember.getChatroomMemberType().equals("관리자")) {
                userChatroomMember = userChatroomMember.setChatroomMemberType("삭제")
                        .builder();
                chatroomMemberRepository.save(userChatroomMember);
                oldChatMemberMap.get(chatroomCode).remove(userEmployeeCode);
                return "채팅방에 나가기 성공했습니다.";
            } else {
                return "해당 채팅방의 관리자이므로 나갈 수 없습니다. 다른 사람에게 권한을 위임해야 합니다.";
            }
        }
    }

    @Transactional
    public ChatDTO sendChat(Long chatroomCode, SendDTO sendDTO) {
        try {
            Long chatroomMemberCode = sendDTO.getChatroomMemberCode();
            LocalDateTime chatWriteDate = sendDTO.getChatWriteDate();
            String chatContent = sendDTO.getChatContent();
            List<MultipartFile> chatFileList = sendDTO.getChatFileList();

            ChatroomMember chatroomMember = chatroomMemberRepository.findById(chatroomMemberCode)
                    .orElseThrow(() -> new DataNotFoundException("현재 데이터베이스에 계정 정보가 없습니다."));

            Chat chat = new Chat()
                    .setChatroomCode(chatroomCode)
                    .setChatroomMember(chatroomMember)
                    .setChatWriteDate(chatWriteDate)
                    .setChatContent(chatContent)
                    .builder();
            if (chatFileList != null && !chatFileList.isEmpty()) {
                LocalDateTime now = LocalDateTime.now();
                List<ChatFile> chatFileEntityList = new ArrayList<>();
                for (MultipartFile chatFile : chatFileList) {
                    String imageName = UUID.randomUUID().toString().replace("-", "");
                    String replaceFileName = null;
                    try {
                        replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, chatFile);
                        ChatFile chatFileEntity = new ChatFile()
                                .setChatFileOgFile(chatFile.getOriginalFilename())
                                .setChatFileChangedFile(replaceFileName)
                                .setChatFileUpdateDate(now)
                                .builder();
                        chatFileEntityList.add(chatFileEntity);
                    } catch (IOException e) {
                        FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
                        throw new DataInsertionException("채팅 사진 저장 실패");
                    }
                }
                chat = chat.setChatFileList(chatFileEntityList);
                chatRepository.save(chat);
            }
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

    /* 채팅 관찰 시점 업데이트*/
    @Transactional
    public String updateChatReadStatus(Long chatCode, Long chatroomCode, Long userEmployeeCode) {
        ChatroomMember chatroomMember = chatroomMemberRepository.findByChatroomCodeAndEmployee_EmployeeCode(chatroomCode, userEmployeeCode)
                .orElseThrow(() -> new DataNotFoundException("현재 계정이 채팅방 멤버 정보에 없습니다."));
        ChatReadStatus chatReadStatus = chatReadStatusRepository.findByChatroomCodeAndChatroomMemberCode(chatroomCode, chatroomMember.getChatroomMemberCode())
                .orElseThrow(() -> new DataNotFoundException("현재 계정이 채팅방 멤버 정보에 없습니다."));
        chatReadStatus.setChatCode(chatCode);

        return "최근 채팅 관찰 시점 갱신 성공";
    }
}