package com.wittypuppy.backend.board.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DemoDTO {
    private Long demoCode;

    @NotNull(message = "이 컬럼에는 데이터가 반드시 들어와야 합니다.")
    private String column1;

    private Long column2;

    private Double column3;

    private LocalDateTime column4;
}
