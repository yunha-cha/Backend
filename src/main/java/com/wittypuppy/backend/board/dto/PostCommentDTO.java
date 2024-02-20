package com.wittypuppy.backend.board.dto;

import com.wittypuppy.backend.board.entity.BoardMember;
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

    private BoardMember boardMember;

    private String postCommentContext;

    private LocalDateTime postCommentDate;

    private LocalDateTime postCommentUpdateDate;

    private String postCommentDeleteStatus;

}
