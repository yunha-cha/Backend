package com.wittypuppy.backend.board.dto;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BoardDeptInfoDTO {

    private Long boardCode;

    private Long departmentCode;

}
