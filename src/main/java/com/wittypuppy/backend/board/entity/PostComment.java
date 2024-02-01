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
@Table(name = "tbl_post_comment")
public class PostComment {
    @Id
    @Column(name = "post_comment_code",columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_comment_code;

    @Column(name = "post_code",columnDefinition = "BIGINT")
    private Long post_code;

    @Column(name = "board_member_code",columnDefinition = "BIGINT")
    private Long board_member_code;

    @Column(name = "post_comment_context",columnDefinition = "VARCHAR(500)")
    private String post_comment_context;

    @Column(name = "post_comment_date",columnDefinition = "DATETIME")
    private LocalDateTime post_comment_date;

    @Column(name = "post_comment_update_date",columnDefinition = "DATETIME")
    private LocalDateTime post_comment_update_date;

    @Column(name = "post_comment_delete_status",columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String post_comment_delete_status;
}
