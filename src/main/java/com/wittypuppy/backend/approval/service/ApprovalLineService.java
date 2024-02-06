package com.wittypuppy.backend.approval.service;

import com.wittypuppy.backend.approval.entity.ApprovalLine;
import com.wittypuppy.backend.approval.repository.ApprovalLineRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApprovalLineService {
    private final ModelMapper modelMapper;
    private final ApprovalLineRepository approvalLineRepository;

    public ApprovalLineService(ModelMapper modelMapper, ApprovalLineRepository approvalLineRepository) {
        this.modelMapper = modelMapper;
        this.approvalLineRepository = approvalLineRepository;
    }

    public void saveApprovalLine(ApprovalLine newApprovalLine) {
    }
}
