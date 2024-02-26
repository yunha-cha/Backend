package com.wittypuppy.backend.board.paging;

import com.wittypuppy.backend.board.dto.PostDTO;
import lombok.*;
import org.springframework.data.domain.Page;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PagingResponseDTO {

    PageDTO page; // 페이지 설정

    Object data; // 받는 데이터

}
