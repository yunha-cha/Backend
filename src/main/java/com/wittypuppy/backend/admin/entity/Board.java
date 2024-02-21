package com.wittypuppy.backend.admin.entity;

import com.wittypuppy.backend.board.entity.BoardMember;
import com.wittypuppy.backend.common.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "ADMIN_BOARD")
@Table(name = "tbl_board")
public class Board {
    @Id
    @Column(name = "board_code",columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardCode;

//    @Column(name = "board_manager_code",columnDefinition = "BIGINT")
//    private Long boardManagerCode;

    @Column(name = "board_group_code",columnDefinition = "BIGINT")
    private Long boardGroupCode;

    @Column(name = "board_title",columnDefinition = "VARCHAR(500)")
    private String boardTitle;

    @Column(name = "board_description",columnDefinition = "VARCHAR(3000)")
    private String boardDescription;

    @Column(name = "board_access_status",columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String boardAccessStatus;

//    @JoinColumn(name = "board_code")
//    @OneToMany
//    private List<Post> postList;

    @JoinColumn(name = "board_code")
    @OneToMany
    private List<BoardMember> boardMemberList;
}
