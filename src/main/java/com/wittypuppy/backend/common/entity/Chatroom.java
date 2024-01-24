package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_chatroom")
public class Chatroom {
    @Id
    @Column(name = "chatroom_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatroomCode;

    @Column(name = "messanger_code", columnDefinition = "BIGINT")
    private Long messangerCode;

    @Column(name = "chatroom_title", columnDefinition = "VARCHAR(100)")
    private String chatroomTitle;

    @Column(name = "chatroom_fixed_status", columnDefinition = "VARCHAR(1)")
    private String chatroomFixedStatus;

    @JoinColumn(name = "chatroom_code")
    @OneToMany
    private List<Chat> chatList;

    @JoinColumn(name = "chatroom_code")
    @OneToMany
    private List<ChatroomMember> chatroomMemberList;

    @JoinColumn(name = "chatroom_code")
    @OneToMany
    private List<ChatroomProfile> chatroomProfileList;
}
