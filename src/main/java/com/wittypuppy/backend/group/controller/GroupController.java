package com.wittypuppy.backend.group.controller;

import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.group.dto.GroupEmpDTO;
import com.wittypuppy.backend.group.service.GroupEmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/group")
@Slf4j
public class GroupController {


    private final GroupEmpService groupEmpService;


    public GroupController(GroupEmpService groupEmpService) {
        this.groupEmpService = groupEmpService;
    }

    @GetMapping("/chart")
    public ResponseEntity<ResponseDTO> findGroupEmpByEmpCode(@PathVariable Long empCode){
        log.info("그룹 컨트롤러에서 그룹리스트 뽑아내기 >> 시작");
//        List<GroupEmpDTO> groupEmpDTOList = groupEmpService.findGroupEmpByEmpCode(empCode);
        return null;
    }


}
