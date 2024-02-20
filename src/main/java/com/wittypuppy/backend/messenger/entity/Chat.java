package com.wittypuppy.backend.messenger.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity(name = "MESSENGER_CHAT")
@Table(name = "tbl_chat")
public class Chat {
    @Id
    @Column(name = "chat_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatCode;

    @Column(name = "chatroom_code")
    private Long chatroomCode;

    @JoinColumn(name="chatroom_member_code")
    @ManyToOne
    private ChatroomMember chatroomMember;

    @Column(name = "chat_write_date")
    private Date chatWriteDate;

    @Column(name = "chat_content")
    private String chatContent;

    @JoinColumn(name = "chat_code")
    @OneToMany(cascade = CascadeType.ALL)
    private List<ChatReadStatus> chatReadStatusList;

    @JoinColumn(name = "chat_code")
    @OneToMany(cascade = CascadeType.ALL)
    private List<ChatFile> chatFileList;


    public Chat setChatCode(Long chatCode) {
        this.chatCode = chatCode;
        return this;
    }

    public Chat setChatroomCode(Long chatroomCode) {
        this.chatroomCode = chatroomCode;
        return this;
    }

    public Chat setChatroomMember(ChatroomMember chatroomMember) {
        this.chatroomMember = chatroomMember;
        return this;
    }

    public Chat setChatWriteDate(Date chatWriteDate) {
        this.chatWriteDate = chatWriteDate;
        return this;
    }

    public Chat setChatContent(String chatContent) {
        this.chatContent = chatContent;
        return this;
    }

    public Chat setChatReadStatusList(List<ChatReadStatus> chatReadStatusList) {
        this.chatReadStatusList = chatReadStatusList;
        return this;
    }

    public Chat setChatFileList(List<ChatFile> chatFileList) {
        this.chatFileList = chatFileList;
        return this;
    }

    public Chat builder() {
        return new Chat(chatCode, chatroomCode, chatroomMember, chatWriteDate, chatContent, chatReadStatusList, chatFileList);
    }
}
