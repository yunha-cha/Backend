package com.wittypuppy.backend.group.controller;

import com.wittypuppy.backend.Employee.dto.User;
import com.wittypuppy.backend.common.dto.Criteria;
import com.wittypuppy.backend.common.dto.PageDTO;
import com.wittypuppy.backend.common.dto.PagingResponseDTO;
import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.group.dto.ChartData;
import com.wittypuppy.backend.group.dto.GroupDeptDTO;
import com.wittypuppy.backend.group.dto.GroupEmpDTO;
import com.wittypuppy.backend.group.entity.GroupDept;
import com.wittypuppy.backend.group.service.GroupEmpService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "조직 페이지 스웨거 연동")
@RestController
@RequestMapping("/api/v1/group")
@Slf4j
public class GroupController {

    private final GroupEmpService groupEmpService;

    public GroupController(GroupEmpService groupEmpService) {
        this.groupEmpService = groupEmpService;
    }

//    조직 들어가면 나오는 그룹리스트
@Tag(name = "사원 리스트 조회" , description = "조직에서 사원 전체 리스트 조회")
    @GetMapping("/chartlist")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO> selectEmpListWithGroupPaging(
            @RequestParam(name = "offset", defaultValue = "1") String offset
            ){

        Criteria criteria = new Criteria(Integer.valueOf(offset), 10);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        Page<GroupEmpDTO> groupList = groupEmpService.selectEmpListWithGroupPaging(criteria);
        pagingResponseDTO.setData(groupList);
        /* 2. PageDTO : 화면에서 페이징 처리에 필요한 정보들 */
        pagingResponseDTO.setPageInfo(new PageDTO(criteria, (int) groupList.getTotalElements()));

        log.info("[그룹컨트롤러 페이징처리 끝] selectProductListWithPaging End ============ ");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", pagingResponseDTO));

    }

    @Tag(name = "사원 검색" , description = "부서와 사원이름을 통한 검색")
    @GetMapping("/chartlist/search")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDTO> selectSearchGroupList(
            @RequestParam(name = "s", defaultValue = "") String search, @AuthenticationPrincipal User principal){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "그룹 조회 성공 테스트", groupEmpService.selectGroupList(search, search)));
    }


    @GetMapping("/chartdata")
    public ResponseEntity<List<ChartData>> getOrgChartData() {
        List<ChartData> orgChartData = groupEmpService.getChartData();
        return ResponseEntity.ok().body(orgChartData);
    }

    @PostMapping("/adddept")
    public ResponseEntity<ResponseDTO> addDepartment(@RequestBody GroupDeptDTO groupDeptDTO) {
        groupEmpService.addDepartment(groupDeptDTO.getDeptName(), groupDeptDTO.getParentDeptCode());
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "부서 추가하기 성공", "부서 추가하기 성공했습니다"));
    }



}
