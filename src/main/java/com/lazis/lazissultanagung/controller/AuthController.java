package com.lazis.lazissultanagung.controller;

import com.lazis.lazissultanagung.dto.response.JwtResponse;
import com.lazis.lazissultanagung.dto.request.SignInRequest;
import com.lazis.lazissultanagung.dto.request.SignUpRequest;
import com.lazis.lazissultanagung.model.Admin;
import com.lazis.lazissultanagung.model.Donatur;
import com.lazis.lazissultanagung.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody SignInRequest signInRequest, HttpServletResponse response) {
        JwtResponse jwtResponse = authService.authenticateUser(signInRequest, response);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody SignUpRequest signUpRequest) {
        Admin admin = authService.registerAdmin(signUpRequest);
        return ResponseEntity.ok(admin);
    }

    @PostMapping("/signup/donatur")
    public ResponseEntity<?> registerDonatur(@RequestBody SignUpRequest signUpRequest) {
        Donatur donatur = authService.registerDonatur(signUpRequest);
        return ResponseEntity.ok(donatur);
    }
}
