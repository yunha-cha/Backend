package com.wittypuppy.backend.group.service;


import com.wittypuppy.backend.group.dto.GroupEmpDTO;
import com.wittypuppy.backend.group.entity.GroupEmp;
import com.wittypuppy.backend.group.repository.GroupEmpRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GroupEmpService {

    private final GroupEmpRepository groupEmpRepository;

    private final ModelMapper modelMapper;

    public GroupEmpService(GroupEmpRepository groupEmpRepository, ModelMapper modelMapper) {
        this.groupEmpRepository = groupEmpRepository;
        this.modelMapper = modelMapper;
    }

//    public GroupEmpDTO findGroupEmpByEmpCode(Long empCode){
//
//
//        GroupEmp groupEmp = groupEmpRepository.findById(empCode);
//        System.out.println("groupEmp = " + groupEmp);
//        GroupEmpDTO groupEmpDTO = modelMapper.map(groupEmp, GroupEmpDTO.class);
//        System.out.println("그룹임프디티오 나오나" + groupEmpDTO);
//        log.info("findGroupByEmpCode 시작");
//        return groupEmpDTO;
//
//    }

}
