package com.wittypuppy.backend.board.dto;
import lombok.*;

import java.time.LocalDateTime;

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

    private LocalDateTime postAttachmentDate;

    private String postDeleteStatus;

}
