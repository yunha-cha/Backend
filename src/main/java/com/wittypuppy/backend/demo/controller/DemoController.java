package com.wittypuppy.backend.demo.controller;


import com.wittypuppy.backend.common.dto.ResponseDTO;
import com.wittypuppy.backend.demo.dto.DemoDTO;
import com.wittypuppy.backend.demo.service.DemoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class DemoController {

    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/demos")
    public ResponseEntity<ResponseDTO> selectDemoList() {
        log.info("DemoController >>> selectDemoList >>> start");

        List<DemoDTO> demoDTOList = demoService.selectDemoList();

        log.info("DemoController >>> selectDemoList >>> end");
        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "조회 성공", demoDTOList));
    }

    @GetMapping("/demos/{demoCode}")
    public ResponseEntity<ResponseDTO> findDemoByNo(@PathVariable Long demoCode) {
        log.info("DemoController >>> findDemoByNo >>> start");

        DemoDTO demoDTO = demoService.findDemoByNo(demoCode);
        log.info("demoDTO >>> " + demoDTO.toString());

        log.info("DemoController >>> findDemoByNo >>> end");
        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "조회 성공", demoDTO));
    }

    @PostMapping("/demos")
    public ResponseEntity<ResponseDTO> insertDemo(@RequestBody @Valid DemoDTO newDemo) {
        log.info("DemoController >>> insertDemo >>> start");

        String data = demoService.insertDemo(newDemo);

        log.info("DemoController >>> insertDemo >>> end");
        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "상품 입력 성공", data));
    }

    @PutMapping("/demos/{demoCode}")
    public ResponseEntity<ResponseDTO> updateDemo(@RequestBody @Valid DemoDTO newDemo, @PathVariable Long demoCode) {
        log.info("DemoController >>> updateDemo >>> start");

        String data = demoService.updateDemo(newDemo, demoCode);

        log.info("DemoController >>> updateDemo >>> end");
        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "상품 입력 성공", data));
    }

    @DeleteMapping("/demos/{demoCode}")
    public ResponseEntity<ResponseDTO> deleteDemoByNo(@PathVariable Long demoCode) {
        log.info("DemoController >>> deleteDemoByNo >>> start");

        String data = demoService.deleteDemoByNo(demoCode);


        log.info("DemoController >>> deleteDemoByNo >>> end");
        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "상품 입력 성공", data));
    }
}
