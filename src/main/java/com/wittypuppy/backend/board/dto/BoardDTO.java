package com.wittypuppy.backend.board.dto;

import com.wittypuppy.backend.board.entity.BoardMember;
import com.wittypuppy.backend.common.entity.Post;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BoardDTO {

    private Long boardCode;

//    private Long boardManagerCode;

    private Long boardGroupCode;

    private String boardTitle;

    private String boardDescription;

    private String boardAccessStatus;

//    private List<PostDTO> postList;

//    private List<BoardMemberDTO> boardMemberList;

}
