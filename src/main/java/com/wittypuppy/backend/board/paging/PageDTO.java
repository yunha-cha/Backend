package com.wittypuppy.backend.board.paging;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PageDTO {

    private int startPage;
    private int endPage;

    private int totalQuantity; // 데이터 전체 개수

    // 이전, 다음 버튼
    private boolean prev;
    private boolean next;


    // 현재 페이지, 한 페이지당 데이터 개수
    private Criteria cri;

    public PageDTO(Criteria cri, int totalQuantity) {
        this.totalQuantity = totalQuantity;
        this.cri = cri;


        /* endPage 계산 */
        // 현재 페이지가 13 일 경우 현재 화면의 끝 페이지는 20으로 만들기 위함
        this.endPage = (int) Math.ceil(cri.getPageNum() / 10.0) * 10;


        /* 마지막 페이지 번호 */
        //전체 마지막 페이지 번호 = (행 전체 개수 * 1) / 행 표시 수
        // 23개 / 10 -> 3페이지까지 나와야 한다.
        int realEndPage = (int) Math.ceil(totalQuantity * 1.0 / cri.getQuantity());


        /* startPage 계산 */
        this.startPage = this.endPage - 9;


        /* endPage를 마지막 페이지 번호로 설정 */
        if(realEndPage < this.endPage){
            this.endPage = realEndPage;
        }


        /* 이전 버튼 값 설정 */
        this.prev = this.startPage > 1; // 페이지가 하나만 있는거 아니면 이전 버튼 표시


        /* 다음 버튼 값 설정 */
        this.next = this.endPage < realEndPage;
        // 현재 페이지의 마지막 번호가 11~20 인데
        // 데이터로 인한 전체 페이지수가 20 이상이면 다음 버튼 생겨야함

    }
}
