package com.wittypuppy.backend.messenger.controller;

import com.wittypuppy.backend.messenger.dto.ChatDTO;
import com.wittypuppy.backend.messenger.dto.SendDTO;
import com.wittypuppy.backend.messenger.service.MessengerService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@AllArgsConstructor
public class MessengerWebsocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessengerService messengerService;
    private final Map<Long, Set<Long>> oldChatMemberMap;
    private final Map<Long, Set<Long>> newChatMemberMap;

    @MessageMapping("/messenger/chatrooms/{chatroomCode}/send")
    public void sendChat(@DestinationVariable Long chatroomCode, @Payload SendDTO sendDTO) {
        /* 1. 메시지 수신 정보 */
        System.out.println("어떤 채팅방? " + chatroomCode);
        System.out.println(">>> 어떤 메시지? " + sendDTO); // 처음에 전달할때는 분명 드라이브에 파일이 저장되어야 하므로.내부적으로 MultipartFile이 있다.

        /* 2. 데이터베이스 저장 + 보낸 채팅을 return.(적절한 형식의 반환값을 얻기 위해) */
        ChatDTO chatDTO = messengerService.sendChat(chatroomCode, sendDTO);

        // 3. 구독한 대상자에게 데이터 전달
        String destination = "/topic/messenger/chatrooms/" + chatroomCode;
        messagingTemplate.convertAndSend(destination, chatDTO);

        /* 4. 만약 초대한 사람이 있다면 구독하게 설정 */
        Set<Long> newChatroomMemberCodeSet = newChatMemberMap.get(chatroomCode);
        if (newChatroomMemberCodeSet != null && !newChatroomMemberCodeSet.isEmpty()) {
            for (Long newChatroomMemberCode : newChatroomMemberCodeSet) {
                // 초대된 사용자에게 알림 전송
                Long employeeCode = messengerService.getEmployeeCode(newChatroomMemberCode);

                destination = "/topic/alert/" + employeeCode; // 계정마다 열려있는 /topic/alert를 기준으로 하는 websocket
                Map<String,Object> returnMessenger = new HashMap<>();
                returnMessenger.put("MessengerSubscribe", chatDTO); // 해당 alert를 인식하기 위한 Map. 객체에 이름을 추가했다고 생각하면 된다.
                messagingTemplate.convertAndSend(destination, returnMessenger); // 그 계정에게 전달. 받으면 내부에 chatroomCode를 확인하고 위의 구독방식을 진행
            }
        }
    }
}