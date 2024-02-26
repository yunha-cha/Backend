package com.wittypuppy.backend.board.dto;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PostAttachmentDTO {

    private Long postAttachmentCode;

    private Long postCode;

    private String postAttachmentOgFile;

    private String postAttachmentChangedFile;

    private Date postAttachmentDate;

    private String postDeleteStatus;

}
