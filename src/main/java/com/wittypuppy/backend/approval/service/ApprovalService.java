package com.wittypuppy.backend.approval.service;

import com.wittypuppy.backend.approval.dto.ApprovalDocDTO;
import com.wittypuppy.backend.approval.entity.ApprovalDoc;
import com.wittypuppy.backend.approval.repository.ApprovalDocRepository;
import com.wittypuppy.backend.attendance.repository.ApprovalRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApprovalService {
    private final ModelMapper modelMapper;
    private final ApprovalDocRepository approvalDocRepository;

    public ApprovalService(ModelMapper modelMapper, ApprovalRepository approvalRepository, ApprovalDocRepository approvalDocRepository) {
        this.modelMapper = modelMapper;
        this.approvalDocRepository = approvalDocRepository;
    }

    public ApprovalDocDTO selectInboxDoc(Long approvalDocCode) {
        log.info("[ApprovalService] selectInboxDoc start=====");

        ApprovalDoc approvalDoc = approvalDocRepository.findById(approvalDocCode).get();
        ApprovalDocDTO approvalDocDTO = modelMapper.map(approvalDoc, ApprovalDocDTO.class);

        log.info("[ApprovalService] selectInboxDoc End=====");

        return approvalDocDTO;
    }
}
