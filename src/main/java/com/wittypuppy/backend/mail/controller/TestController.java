package com.wittypuppy.backend.mail.controller;


import com.wittypuppy.backend.Employee.dto.EmployeeDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("unread-email-counts")
    public EmployeeDTO countUnreadEmail(@AuthenticationPrincipal EmployeeDTO principal){

        System.out.println("principal = " + principal);

        return principal;
    }
}
