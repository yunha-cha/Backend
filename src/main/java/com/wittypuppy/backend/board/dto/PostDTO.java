package com.wittypuppy.backend.board.dto;

import com.wittypuppy.backend.board.entity.PostAlert;
import com.wittypuppy.backend.board.entity.PostAttachment;
import com.wittypuppy.backend.board.entity.PostComment;
import com.wittypuppy.backend.board.entity.PostLike;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PostDTO {
    private Long postCode;

    private Long boardCode;

    private EmployeeDTO employee;

    private String postNoticeStatus;

    private String postTitle;


    private String postContext;


    private Long postViews;

    private Long postLikes;


    private LocalDateTime postDate;


    private String postStorageStatus;


//    private List<PostAlertDTO> postAlertList;


    private List<PostLikeDTO> postLikeList;


    private List<PostAttachmentDTO> postAttachmentList;


    private List<PostCommentDTO> postCommentList;

}
