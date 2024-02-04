package com.wittypuppy.backend.admin.dto;

import com.wittypuppy.backend.board.dto.BoardMemberDTO;
import com.wittypuppy.backend.board.dto.PostDTO;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BoardDTO {

    private Long boardCode;

    private Long boardManagerCode;

    private Long boardGroupCode;

    private String boardTitle;

    private String boardDescription;

    private String boardAccessStatus;

    private List<PostDTO> postList;

    private List<BoardMemberDTO> boardMemberList;

}
