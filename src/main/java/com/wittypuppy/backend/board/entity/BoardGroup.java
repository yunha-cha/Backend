package com.wittypuppy.backend.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "BOARD_BOARD_GROUP")
@Table(name = "tbl_board_group")
public class BoardGroup {
    @Id
    @Column(name = "board_group_code",columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardGroupCode;

    @Column(name = "board_group_title",columnDefinition = "VARCHAR(100)")
    private String boardGroupName;

    @JoinColumn(name = "board_group_code")
    @OneToMany(fetch = FetchType.LAZY) // 참조 안하면 안부름
    private List<Board> boardList;
}
