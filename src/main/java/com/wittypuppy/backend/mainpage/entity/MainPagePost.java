package com.wittypuppy.backend.mainpage.entity;

import com.wittypuppy.backend.board.entity.PostAttachment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "MAINPAGE_BOARD_POST")
@Table(name = "tbl_post")
public class MainPagePost {
    @Id
    @Column(name = "post_code",columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postCode;

    @Column(name = "board_code",columnDefinition = "BIGINT")
    private Long boardCode;

    @Column(name = "employee_code",columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "post_notice_status",columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String postNoticeStatus;

    @Column(name = "post_title",columnDefinition = "VARCHAR(100)")
    private String postTitle;

    @Column(name = "post_context",columnDefinition = "VARCHAR(3000)")
    private String postContext;

    @Column(name = "post_views",columnDefinition = "BIGINT")
    private Long postViews;

    @Column(name = "post_date",columnDefinition = "DATETIME")
    private LocalDateTime postDate;

    @Column(name = "post_storage_status",columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String postStorageStatus;

    @JoinColumn(name = "post_code")
    @OneToMany(fetch = FetchType.LAZY)
    private List<PostAttachment> postAttachmentList;



}
