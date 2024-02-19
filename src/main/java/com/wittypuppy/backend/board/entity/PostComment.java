package com.wittypuppy.backend.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "BOARD_POST_COMMENT")
@Table(name = "tbl_post_comment")
public class PostComment {

    @Id
    @Column(name = "post_comment_code",columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postCommentCode;

    @Column(name = "post_code",columnDefinition = "BIGINT")
    private Long postCode;

    @JoinColumn(name = "board_member_code")
    @ManyToOne
    private BoardMember boardMember;

    @Column(name = "post_comment_context",columnDefinition = "VARCHAR(500)")
    private String postCommentContext;

    @Column(name = "post_comment_date",columnDefinition = "DATETIME")
    private LocalDateTime postCommentDate;

    @Column(name = "post_comment_update_date",columnDefinition = "DATETIME")
    private LocalDateTime postCommentUpdateDate;

    @Column(name = "post_comment_delete_status",columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String postCommentDeleteStatus;

}
