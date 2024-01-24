package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_chat_read_status")
public class ChatReadStatus {
    @Id
    @Column(name = "chat_read_status_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatReadStatusCode;

    @Column(name = "chatroom_member_code", columnDefinition = "BIGINT")
    private Long chatroomMemberCode;

    @Column(name = "chat_code", columnDefinition = "BIGINT")
    private Long chatCode;
}
