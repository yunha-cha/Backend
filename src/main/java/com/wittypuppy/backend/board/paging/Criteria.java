package com.wittypuppy.backend.board.paging;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {

    private int pageNum;
    private int quantity;
    private String searchValue;

    public Criteria(){
        this(1, 10);  // 1페이지, 10개 게시글
    }

    public Criteria(int pageNum, int quantity) {
        this.pageNum = pageNum;
        this.quantity = quantity;
    }

}
