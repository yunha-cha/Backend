package com.wittypuppy.backend.messenger.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity(name = "MESSENGER_CHATROOM_MEMBER")
@Table(name = "tbl_chatroom_member")
public class ChatroomMember {
    @Id
    @Column(name = "chatroom_member_code")
    private Long chatroomMemberCode;

    @Column(name = "chatroom_code")
    private Long chatroomCode;

    @JoinColumn(name = "employee_code")
    @ManyToOne
    private Employee employee;

    @Column(name = "chatroom_member_type")
    private String chatroomMemberType;

    @Column(name = "chatroom_member_invite_time")
    private LocalDateTime chatroomMemberInviteTime;

    @JoinColumn(name = "chatroom_member_code")
    @OneToMany(cascade = CascadeType.ALL)
    private List<ChatReadStatus> chatReadStatusList;

    @JoinColumn(name = "chatroom_member_code")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Chat> chatList;

    public ChatroomMember setChatroomMemberCode(Long chatroomMemberCode) {
        this.chatroomMemberCode = chatroomMemberCode;
        return this;
    }

    public ChatroomMember setChatroomCode(Long chatroomCode) {
        this.chatroomCode = chatroomCode;
        return this;
    }

    public ChatroomMember setEmployee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public ChatroomMember setChatroomMemberType(String chatroomMemberType) {
        this.chatroomMemberType = chatroomMemberType;
        return this;
    }

    public ChatroomMember setChatroomMemberInviteTime(LocalDateTime chatroomMemberInviteTime) {
        this.chatroomMemberInviteTime = chatroomMemberInviteTime;
        return this;
    }

    public ChatroomMember setChatReadStatusList(List<ChatReadStatus> chatReadStatusList) {
        this.chatReadStatusList = chatReadStatusList;
        return this;
    }

    public ChatroomMember setChatList(List<Chat> chatList) {
        this.chatList = chatList;
        return this;
    }

    public ChatroomMember builder() {
        return new ChatroomMember(chatroomMemberCode, chatroomCode, employee, chatroomMemberType, chatroomMemberInviteTime, chatReadStatusList, chatList);
    }
}
