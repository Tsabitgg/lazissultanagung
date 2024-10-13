package com.lazis.lazissultanagung.controller;

import com.lazis.lazissultanagung.model.Admin;
import com.lazis.lazissultanagung.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUser(){
        Admin currentAdmin = adminService.getCurrentAdmin();
        return ResponseEntity.ok(currentAdmin);
    }

    @GetMapping("/get-all-operator")
    public Page<Map<String, Object>> getAllOperator(@RequestParam(name = "page", defaultValue = "0") int page) {
        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return adminService.getAllOperator(pageRequest);
    }
}
