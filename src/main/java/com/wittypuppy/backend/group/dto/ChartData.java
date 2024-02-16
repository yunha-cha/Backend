package com.wittypuppy.backend.group.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChartData {
    private String id;
    private String parent;
    private String text;
    private ChartState state;
    private String type;
//    private List<ChartData> children; // 자식 노드 목록


}
