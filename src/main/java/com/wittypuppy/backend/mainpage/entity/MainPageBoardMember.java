package com.wittypuppy.backend.mainpage.entity;

import com.wittypuppy.backend.common.entity.PostAlert;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "MAINPAGE_BOARD_MEMBER")
@Table(name = "tbl_board_member")
public class MainPageBoardMember {
    @Id
    @Column(name = "board_member_code", columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardMemberCode;

    @Column(name = "employee_code", columnDefinition = "BIGINT")
    private Long employeeCode;

    @Column(name = "board_code", columnDefinition = "BIGINT")
    private Long boardCode;

    @Column(name = "board_member_create_post_status", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String boardMemberCreatePostStatus;

//    @JoinColumn(name = "board_member_code")
//    @OneToMany
//    private List<PostComment> postCommentList;

    @JoinColumn(name = "board_member_code")
    @OneToMany
    private List<PostAlert> postAlertList;
}
