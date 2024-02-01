package com.wittypuppy.backend.board.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PostCommentDTO {

    private Long postCommentCode;

    private Long postCode;

    private Long boardMemberCode;

    private String postCommentContext;

    private LocalDateTime postCommentDate;

    private LocalDateTime postCommentUpdateDate;

    private String postCommentDeleteStatus;

}
