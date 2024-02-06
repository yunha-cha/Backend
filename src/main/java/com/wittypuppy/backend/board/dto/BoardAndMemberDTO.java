package com.wittypuppy.backend.board.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BoardAndMemberDTO {

    BoardDTO board;

    List<BoardMemberDTO> memberList;
}
