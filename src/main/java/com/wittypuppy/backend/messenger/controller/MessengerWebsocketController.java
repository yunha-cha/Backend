package com.wittypuppy.backend.messenger.controller;

import com.wittypuppy.backend.common.exception.TokenException;
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
        if (token != null) {
            send.setEmployeeCode(tokenUtils.getUserEmployeeCode(token));
        } else {
            throw new TokenException("허가되지 않은 채팅 전송");
        }
        ChatDTO chatDTO = messengerService.sendChat(Long.parseLong(chatroomCode), send);

        String destination = "/topic/messenger/chatrooms/" + chatroomCode;
        messagingTemplate.convertAndSend(destination, chatDTO);
    }

    @MessageMapping("/messenger/chatrooms/{chatroomCode}/invite")
    public void inviteChatroom(
            @DestinationVariable String chatroomCode,
            @Payload Map<String, Object> employeeMap,
            SimpMessageHeaderAccessor accessor) {
        String token = accessor.getFirstNativeHeader("Authorization");
        TokenUtils tokenUtils = new TokenUtils();
        int userEmployeeCode = 0;
        if (token != null) {
            userEmployeeCode = tokenUtils.getUserEmployeeCode(token);
        } else {
            throw new TokenException("허가되지 않은 채팅방 초대");
        }

        ChatroomMessengerMainInterface result =
                messengerService.newChatroom(Long.valueOf(String.valueOf(userEmployeeCode)), Long.parseLong(chatroomCode));

        String destination = "/topic/messenger/" + employeeMap.get("employeeCode");
        messagingTemplate.convertAndSend(destination, result);
    }
}