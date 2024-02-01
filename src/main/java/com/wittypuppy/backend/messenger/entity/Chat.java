package com.wittypuppy.backend.messenger.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@Entity(name = "MESSENGER_CHAT")
@Table(name = "tbl_chat")
public class Chat {
    @Id
    @Column(name = "chat_code")
    private Long chatCode;

    @Column(name = "chatroom_code")
    private Long chatroomCode;

    @Column(name = "chatroom_member_code")
    private Long chatroomMemberCode;

    @Column(name = "chat_write_date")
    private LocalDateTime chatWriteDate;

    @Column(name = "chat_content")
    private String chatContent;

    @JoinColumn(name = "chat_code")
    @OneToMany
    private List<ChatReadStatus> chatReadStatusList;

    @JoinColumn(name="chat_code")
    @OneToMany
    private List<ChatFile> chatFileList;
}
