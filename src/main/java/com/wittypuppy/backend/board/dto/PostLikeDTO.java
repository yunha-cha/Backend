package com.wittypuppy.backend.board.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PostLikeDTO {

    private Long postLikeCode;

    private Long postCode;

    private Long employeeCode;

}
