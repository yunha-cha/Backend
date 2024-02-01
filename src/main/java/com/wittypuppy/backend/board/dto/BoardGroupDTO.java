package com.wittypuppy.backend.board.dto;

import com.wittypuppy.backend.board.entity.Board;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BoardGroupDTO {

    private Long boardGroupCode;

    private String boardGroupName;

    private List<BoardDTO> boardList;

}
