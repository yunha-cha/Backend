package com.wittypuppy.backend.messenger.controller;

import com.wittypuppy.backend.messenger.dto.ChatDTO;
import com.wittypuppy.backend.messenger.dto.ChatroomMessengerMainInterface;
import com.wittypuppy.backend.messenger.dto.SendDTO;
import com.wittypuppy.backend.messenger.service.MessengerService;
import com.wittypuppy.backend.util.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@AllArgsConstructor
public class MessengerWebsocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessengerService messengerService;

    @MessageMapping("/messenger/chatrooms/{chatroomCode}/send")
    public void sendChat(
            @DestinationVariable String chatroomCode,
            @Payload SendDTO send,
            SimpMessageHeaderAccessor accessor) {
        String token = accessor.getFirstNativeHeader("Authorization");
        TokenUtils tokenUtils = new TokenUtils();
        System.out.println("chatroomCode>>>" + chatroomCode);
        System.out.println("send>>>" + send);
        if (token != null) {
            send.setEmployeeCode(tokenUtils.getUserEmployeeCode(token));
        } else {
            System.out.println("토큰이 없다.");
        }
        /* 2. 데이터베이스 저장 + 보낸 채팅을 return.(적절한 형식의 반환값을 얻기 위해) */
        ChatDTO chatDTO = messengerService.sendChat(Long.parseLong(chatroomCode), send);

        // 3. 구독한 대상자에게 데이터 전달
        String destination = "/topic/messenger/chatrooms/" + chatroomCode;
        System.out.println("destination>>>" + destination);
        messagingTemplate.convertAndSend(destination, chatDTO);
    }

    @MessageMapping("/messenger/chatrooms/{chatroomCode}/invite")
    public void inviteChatroom(
            @DestinationVariable String chatroomCode,
            @Payload Map<String, Object> employeeMap,
            SimpMessageHeaderAccessor accessor) {
        String token = accessor.getFirstNativeHeader("Authorization");
        TokenUtils tokenUtils = new TokenUtils();
        System.out.println("chatroomCode>>>" + chatroomCode);
        System.out.println("employeeCode>>>" + employeeMap.get("employeeCode"));
        int userEmployeeCode = 0;
        if (token != null) {
            userEmployeeCode = tokenUtils.getUserEmployeeCode(token);
        } else {
            System.out.println("토큰이 없다.");
        }

        // 2. 초대는 이미 진행했고. Messenger 메인화면에 들어가는 채팅방 정보를 반환해서 전달한다.
        ChatroomMessengerMainInterface result =
                messengerService.newChatroom(Long.valueOf(String.valueOf(userEmployeeCode)), Long.parseLong(chatroomCode));

        // 3. 구독한 대상자에게 데이터 전달
        String destination = "/topic/messenger/" + employeeMap.get("employeeCode");
        System.out.println("destination>>>" + destination);
        messagingTemplate.convertAndSend(destination, result);
    }
}