package com.wittypuppy.backend.messenger.entity;

import com.wittypuppy.backend.messenger.dto.ChatroomOptionsDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "MESSENGER_CHATROOM_MEMBER")
@Table(name = "tbl_chatroom_member")
public class ChatroomMember {
    @Id
    @Column(name = "chatroom_member_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatroomMemberCode;

    @Column(name = "chatroom_code")
    private Long chatroomCode;

    @JoinColumn(name = "employee_code")
    @ManyToOne
    private Employee employee;

    @Column(name = "chatroom_member_type")
    private String chatroomMemberType;

    @Column(name = "chatroom_member_invite_time")
    private Date chatroomMemberInviteTime;

    @Column(name = "chatroom_member_pinned_status")
    private String chatroomMemberPinnedStatus;

    @JoinColumn(name = "chatroom_member_code")
    @OneToMany(cascade = CascadeType.ALL)
    private List<ChatReadStatus> chatReadStatusList;

    @OneToMany(mappedBy = "chatroomMember", cascade = CascadeType.ALL)
    private List<Chat> chatList;

    @Override
    public String toString() {
        return "ChatroomMember{" +
                "chatroomMemberCode=" + chatroomMemberCode +
                ", chatroomCode=" + chatroomCode +
                ", employee=" + employee +
                ", chatroomMemberType='" + chatroomMemberType + '\'' +
                ", chatroomMemberInviteTime=" + chatroomMemberInviteTime +
                ", chatroomMemberPinnedStatus='" + chatroomMemberPinnedStatus + '\'' +
                ", chatReadStatusList=" + chatReadStatusList +
                '}';
    }

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

    public ChatroomMember setChatroomMemberInviteTime(Date chatroomMemberInviteTime) {
        this.chatroomMemberInviteTime = chatroomMemberInviteTime;
        return this;
    }

    public ChatroomMember setChatroomMemberPinnedStatus(String chatroomMemberPinnedStatus) {
        this.chatroomMemberPinnedStatus = chatroomMemberPinnedStatus;
        return this;
    }

    public ChatroomMember setChatReadStatusList(List<ChatReadStatus> chatReadStatusList) {
        this.chatReadStatusList = chatReadStatusList;
        return this;
    }

//    public ChatroomMember setChatList(List<Chat> chatList) {
//        this.chatList = chatList;
//        return this;
//    }

    public ChatroomMember builder() {
        return new ChatroomMember(chatroomMemberCode, chatroomCode, employee, chatroomMemberType, chatroomMemberInviteTime, chatroomMemberPinnedStatus, chatReadStatusList, chatList);
    }
}
