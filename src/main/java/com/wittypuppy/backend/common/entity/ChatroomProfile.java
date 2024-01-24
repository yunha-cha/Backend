package com.wittypuppy.backend.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_chatroom_profile")
public class ChatroomProfile {
    @Id
    @Column(name = "chatroom_profile_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatroomProfileCode;

    @Column(name = "chatroom_code", columnDefinition = "BIGINT")
    private Long chatroomCode;

    @Column(name = "chatroom_profile_og_file", columnDefinition = "VARCHAR(500)")
    private String chatroomProfileOgFile;

    @Column(name = "chatroom_profile_changed_file", columnDefinition = "VARCHAR(500)")
    private String chatroomProfileChangedFile;

    @Column(name = "chatroom_profile_regist_date", columnDefinition = "DATETIME")
    private LocalDateTime chatroomProfileRegistDate;

    @Column(name = "chatroom_profile_delete_status", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String chatroomProfileDeleteStatus;
}
