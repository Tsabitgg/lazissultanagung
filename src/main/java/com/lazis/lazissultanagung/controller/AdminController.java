package com.lazis.lazissultanagung.controller;

import com.lazis.lazissultanagung.model.Admin;
import com.lazis.lazissultanagung.model.Donatur;
import com.lazis.lazissultanagung.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
