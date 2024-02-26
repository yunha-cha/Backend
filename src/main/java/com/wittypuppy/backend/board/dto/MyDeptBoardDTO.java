package com.wittypuppy.backend.board.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MyDeptBoardDTO {

    private List<BoardDTO> boardList1;
    private List<BoardDTO> boardList2;


}
