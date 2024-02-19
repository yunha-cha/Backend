package com.wittypuppy.backend.messenger.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity(name = "MESSENGER_CHAT_READ_STATUS")
@Table(name = "tbl_chat_read_status")
public class ChatReadStatus {
    @Id
    @Column(name = "chat_read_status_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatReadStatusCode;

    @Column(name = "chatroom_member_code")
    private Long chatroomMemberCode;

    @Column(name = "chat_code")
    private Long chatCode;

    @Column(name = "chatroom_code")
    private Long chatroomCode;

    public ChatReadStatus setChatReadStatusCode(Long chatReadStatusCode) {
        this.chatReadStatusCode = chatReadStatusCode;
        return this;
    }

    public ChatReadStatus setChatroomMemberCode(Long chatroomMemberCode) {
        this.chatroomMemberCode = chatroomMemberCode;
        return this;
    }

    public ChatReadStatus setChatCode(Long chatCode) {
        this.chatCode = chatCode;
        return this;
    }

    public ChatReadStatus setChatroomCode(Long chatroomCode) {
        this.chatroomCode = chatroomCode;
        return this;
    }

    public ChatReadStatus builder() {
        return new ChatReadStatus(chatReadStatusCode, chatroomMemberCode, chatCode, chatroomCode);
    }
}
