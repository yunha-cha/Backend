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
@Table(name = "tbl_chat")
public class Chat {
    @Id
    @Column(name = "chat_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatCode;

    @Column(name = "chatroom_code", columnDefinition = "BIGINT")
    private Long chatroomCode;

    @Column(name = "chatroom_member_code", columnDefinition = "BIGINT")
    private Long chatroomMemberCode;

    @Column(name = "chat_write_date", columnDefinition = "DATETIME")
    private LocalDateTime chatWriteDate;

    @Column(name = "chat_content", columnDefinition = "VARCHAR(500)")
    private String chatContent;

    @JoinColumn(name = "chat_code")
    @OneToMany
    private List<ChatFile> chatFileList;

    @JoinColumn(name = "chat_code")
    @OneToMany
    private List<ChatReadStatus> chatReadStatusList;
}
