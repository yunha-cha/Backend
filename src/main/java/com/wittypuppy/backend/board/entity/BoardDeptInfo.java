package com.wittypuppy.backend.board.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "BOARD_BOARD_DEPT_INFO")
@Table(name = "tbl_board_dept_info")
public class BoardDeptInfo {

    @Id
    @Column(name = "board_dept_info_code",columnDefinition = "BIGINT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardDeptCode;


    @Column(name = "board_code",columnDefinition = "BIGINT")
    private Long boardCode;


    @Column(name = "department_code",columnDefinition = "BIGINT")
    private Long departmentCode;

}
