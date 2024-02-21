package com.wittypuppy.backend.messenger.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity(name = "MESSENGER_CHATROOM")
@Table(name = "tbl_chatroom")
public class Chatroom {
    @Id
    @Column(name = "chatroom_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatroomCode;

    @Column(name = "chatroom_title")
    private String chatroomTitle;

    @Column(name = "chatroom_fixed_status")
    private String chatroomFixedStatus;

    @JoinColumn(name = "chatroom_code")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ChatroomMember> chatroomMemberList;

    @JoinColumn(name = "chatroom_code")
    @OneToMany(cascade = CascadeType.ALL)
    private List<ChatroomProfile> chatroomProfileList;

    @JoinColumn(name = "chatroom_code")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Chat> chatList;

    @JoinColumn(name = "chatroom_code")
    @OneToMany(cascade = CascadeType.ALL)
    private List<ChatReadStatus> chatReadStatusList;

    public Chatroom setChatroomCode(Long chatroomCode) {
        this.chatroomCode = chatroomCode;
        return this;
    }

    public Chatroom setChatroomTitle(String chatroomTitle) {
        this.chatroomTitle = chatroomTitle;
        return this;
    }

    public Chatroom setChatroomFixedStatus(String chatroomFixedStatus) {
        this.chatroomFixedStatus = chatroomFixedStatus;
        return this;
    }

    public Chatroom setChatroomMemberList(List<ChatroomMember> chatroomMemberList) {
        this.chatroomMemberList = chatroomMemberList;
        return this;
    }

    public Chatroom setChatroomProfileList(List<ChatroomProfile> chatroomProfileList) {
        this.chatroomProfileList = chatroomProfileList;
        return this;
    }

    public Chatroom setChatList(List<Chat> chatList) {
        this.chatList = chatList;
        return this;
    }

    public Chatroom setChatReadStatusList(List<ChatReadStatus> chatReadStatusList) {
        this.chatReadStatusList = chatReadStatusList;
        return this;
    }

    public Chatroom builder() {
        return new Chatroom(chatroomCode, chatroomTitle, chatroomFixedStatus, chatroomMemberList, chatroomProfileList, chatList, chatReadStatusList);
    }
}