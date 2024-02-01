package com.wittypuppy.backend.board.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PostAlertDTO {

    private Long postAlertCode;

    private Long postCode;

    private Long boardMemberCode;

    private String postAlertType;

    private String postAlertDatetime;

    private String postAlertContext;

    private String postAlertCheck;

}
