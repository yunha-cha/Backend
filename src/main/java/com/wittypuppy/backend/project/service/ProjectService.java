package com.wittypuppy.backend.project.service;

import com.wittypuppy.backend.project.dto.ProjectAndProjectMemberDTO;
import com.wittypuppy.backend.project.entity.ProjectAndProjectMember;
import com.wittypuppy.backend.project.repository.ProjectAndProjectMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProjectService {
    private final ProjectAndProjectMemberRepository projectAndProjectMemberRepository;
    private final ModelMapper modelMapper;

    public ProjectService(ProjectAndProjectMemberRepository projectAndProjectMemberRepository, ModelMapper modelMapper) {
        this.projectAndProjectMemberRepository = projectAndProjectMemberRepository;
        this.modelMapper = modelMapper;
    }

    public List<ProjectAndProjectMemberDTO> selectProductList() {
        log.info("[ProjectService] >>> selectProductListByEmployeeCode >>> start");

        List<ProjectAndProjectMember> result = projectAndProjectMemberRepository.findAll();
        log.info("ProjectAndProjectMember >>> {} ", result);

        List<ProjectAndProjectMemberDTO> resultDTO = result.stream().map(e -> modelMapper.map(e, ProjectAndProjectMemberDTO.class)).toList();
        log.info("ProjectAndProjectMemberDTO >>> {} ", result);

        log.info("[ProjectService] >>> selectProductListByEmployeeCode >>> end");
        return resultDTO;
    }
}
