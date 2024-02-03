package com.wittypuppy.backend.group.controller;

import com.wittypuppy.backend.common.dto.Criteria;
import com.wittypuppy.backend.common.dto.PageDTO;
import com.wittypuppy.backend.common.dto.PagingResponseDTO;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.group.dto.GroupEmpDTO;
import com.wittypuppy.backend.group.service.GroupEmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
@Slf4j
public class GroupController {


    private final GroupEmpService groupEmpService;


    public GroupController(GroupEmpService groupEmpService) {
        this.groupEmpService = groupEmpService;
    }

//    조직 들어가면 나오는 그룹리스트
    @GetMapping("/chartlist")
    public ResponseEntity<ResponseDTO> selectEmpListWithGroupPaging(
            @RequestParam(name = "offset", defaultValue = "1") String offset
    ){

        log.info("[그룹컨트롤러 시작] selectProductListWithPaging Start ============ ");
        log.info("[offset 나오는지 확인용] selectProductListWithPaging offset : {} ", offset);

        Criteria criteria = new Criteria(Integer.valueOf(offset), 10);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        Page<GroupEmpDTO> productList = groupEmpService.selectEmpListWithGroupPaging(criteria);
        pagingResponseDTO.setData(productList);
        /* 2. PageDTO : 화면에서 페이징 처리에 필요한 정보들 */
        pagingResponseDTO.setPageInfo(new PageDTO(criteria, (int) productList.getTotalElements()));

        log.info("[그룹컨트롤러 페이징처리 끝] selectProductListWithPaging End ============ ");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", pagingResponseDTO));

    }

    @GetMapping("chartlist/search")
    public ResponseEntity<ResponseDTO> selectSearchGroupList(
            @RequestParam(name = "s", defaultValue = "") String search){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "그룹 조회 성공 테스트", groupEmpService.selectGroupList(search, search)));
    }




}
