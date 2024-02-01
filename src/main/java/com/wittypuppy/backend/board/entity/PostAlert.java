package com.wittypuppy.backend.board.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "BOARD_POST_ALERT")
@Table(name = "tbl_post_alert")
public class PostAlert {
    @Id
    @Column(name = "post_alert_code",columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postAlertCode;

    @Column(name = "post_code",columnDefinition = "BIGINT")
    private Long postCode;

    @Column(name = "board_member_code",columnDefinition = "BIGINT")
    private Long boardMemberCode;

    @Column(name = "post_alert_type",columnDefinition = "VARCHAR(100)")
    private String postAlertType;

    @Column(name = "post_alert_datetime",columnDefinition = "DATETIME")
    private String postAlertDatetime;

    @Column(name = "post_alert_context",columnDefinition = "VARCHAR(500)")
    private String postAlertContext;

    @Column(name = "post_alert_check",columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String postAlertCheck;
}
