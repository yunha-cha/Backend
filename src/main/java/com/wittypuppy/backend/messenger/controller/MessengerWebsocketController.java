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

@Controller
@AllArgsConstructor
public class MessengerWebsocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessengerService messengerService;

    @MessageMapping("/messenger/chatrooms/{chatroomCode}/send")
    public void sendChat(@DestinationVariable Long chatroomCode, @Payload SendDTO sendDTO) {
        /* 1. 메시지 수신 정보 */
        System.out.println("어떤 채팅방? " + chatroomCode);
        System.out.println(">>> 어떤 메시지? " + sendDTO); // 처음에 전달할때는 분명 드라이브에 파일이 저장되어야 하므로.내부적으로 MultipartFile이 있다.

        /* 2. 데이터베이스 저장 + 보낸 채팅을 return.(적절한 형식의 반환값을 얻기 위해) */
//        ChatDTO chatDTO = messengerService.sendChat(chatroomCode, sendDTO);

        /* 3. 만약 초대한 사람이 있다면 강제 구독. */
    }
}
//
//    @MessageMapping("/messenger/rooms/{roomCode}/send")
//    public void sendMessage(@DestinationVariable Long roomId, @Payload MessageDTO message) {
//        // 1. 메시지 수신
//        System.out.println("Received message from " + message.getFrom() + " in room " + roomId);
//
//        // 2. 데이터베이스 저장
//        chatRoomService.saveMessage(roomId, message);
//
//        // 3. B 사용자 강제 구독
//        ChatRoom chatRoom = chatRoomService.findChatRoomById(roomId);
//        List<User> usersInRoom = chatRoom.getUsers(); // 채팅방에 있는 사용자 목록 가져오기
//        for (User user : usersInRoom) {
//            if (!user.getUserId().equals(message.getFrom())) { // 보낸 사람을 제외한 사용자
//                subscribeToChatRoom(user.getUserId(), roomId); // B 사용자 강제 구독
//            }
//        }
//
//        // 4. 구독한 대상자에게 데이터 전달
//        String destination = "/topic/messenger/rooms/" + roomId;
//        messagingTemplate.convertAndSend(destination, message);
//    }
//
//    @PostMapping("/subscribe")
//    public ResponseEntity<String> subscribeToChatRoom(@RequestParam("userId") String userId,
//                                                      @RequestParam("roomId") Long roomId) {
//        // 강제로 사용자를 채팅방에 구독시키는 로직을 구현
//        // 여기서는 간단하게 사용자를 채팅방에 추가하는 작업만 수행
//
//        User user = userRepository.findById(userId).orElse(null);
//        if (user != null) {
//            ChatRoom chatRoom = chatRoomService.findChatRoomById(roomId);
//            if (chatRoom != null) {
//                chatRoom.addUser(user); // 채팅방에 사용자 추가
//                chatRoomService.saveChatRoom(chatRoom); // 채팅방 업데이트
//                return ResponseEntity.ok("Subscribed to chat room successfully");
//            }
//        }
//        return ResponseEntity.badRequest().body("Failed to subscribe to chat room");
//    }
//}
