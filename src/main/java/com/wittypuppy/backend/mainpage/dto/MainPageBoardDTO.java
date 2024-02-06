package com.wittypuppy.backend.mainpage.dto;

import com.wittypuppy.backend.board.dto.PostAttachmentDTO;
import com.wittypuppy.backend.board.dto.PostCommentDTO;
import com.wittypuppy.backend.board.dto.PostLikeDTO;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@ToString
public class MainPageBoardDTO {

    private Long boardCode;
    private Long postCode;
    private String boardTitle;
//    private String boardDescription;//내용
    private String postTitle;
    private String postContext;
    private LocalDateTime postDate;
    private Long postViews; //조회수

    private Long employeeCode;

    private String postNoticeStatus;

//    private List<PostAttachmentDTO> postAttachmentList;

}
