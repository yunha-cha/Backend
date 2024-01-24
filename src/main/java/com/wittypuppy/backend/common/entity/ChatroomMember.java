package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_chatroom_member")
public class ChatroomMember {
    @Id
    @Column(name = "chatroom_member_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatroomMemberCode;

    @Column(name = "chatroom_code", columnDefinition = "BIGINT")
    private Long chatroomCode;

    @Column(name = "employee_code", columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "chatroom_member_type", columnDefinition = "VARCHAR(100)")
    private String chatroomMemberType;

    @Column(name = "chatroom_member_invite_time", columnDefinition = "DATETIME")
    private LocalDateTime chatroomMemberInviteTime;

    @JoinColumn(name = "chatroom_member_code")
    @OneToMany
    private List<ChatReadStatus> chatReadStatusList;

    @JoinColumn(name = "chatroom_member_code")
    @OneToMany
    private List<Chat> chatList;
}
