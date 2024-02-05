//package com.wittypuppy.backend.messenger;
//
//import com.wittypuppy.backend.messenger.dto.MessageDTO;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class MessengerController {
//
//    @MessageMapping("/messenger/rooms/{roomId}/send")
//    @SendTo("/topic/messenger/rooms/{roomId}")
//    public MessageDTO sendMessage(@DestinationVariable Long roomId, @Payload MessageDTO message) {
//        System.out.println("roomId: " + roomId);
//        System.out.println("MessengerMessage: " + message);
//        System.out.println(message.getFrom());
//        System.out.println(message.getText());
//        return message;
//    }
//
//}
