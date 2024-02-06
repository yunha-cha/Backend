package com.wittypuppy.backend.mainpage.entity;

import com.wittypuppy.backend.board.entity.Board;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "MAINPAGE_BOARD_GROUP")
@Table(name = "tbl_board_group")
public class MainPageBoardGroup {

    @Id
    @Column(name = "board_group_code",columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardGroupCode;

    @Column(name = "board_group_name",columnDefinition = "VARCHAR(100)")
    private String boardGroupName;

    @JoinColumn(name = "board_group_code")
    @OneToMany
    private List<MainPageBoard> boardList;
}
