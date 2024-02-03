package com.wittypuppy.backend.messenger.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Entity(name = "MESSENGER_CHAT_READ_STATUS")
@Table(name = "tbl_chat_read_status")
public class ChatReadStatus {
    @Id
    @Column(name = "chat_read_status_code")
    private Long chatReadStatusCode;

    @Column(name = "chatroom_member_code")
    private Long chatroomMemberCode;

    @Column(name = "chat_code")
    private Long chatCode;
}
