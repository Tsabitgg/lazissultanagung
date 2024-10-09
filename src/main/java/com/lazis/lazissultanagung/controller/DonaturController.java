package com.lazis.lazissultanagung.controller;

import com.lazis.lazissultanagung.dto.request.EditProfileDonaturRequest;
import com.lazis.lazissultanagung.model.Donatur;
import com.lazis.lazissultanagung.service.DonaturService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("api/donatur")
public class DonaturController {

    @Autowired
    private DonaturService donaturService;

    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUser(){
        Donatur currentDonatur = donaturService.getCurrentDonatur();
        return ResponseEntity.ok(currentDonatur);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editUserProfile(@ModelAttribute EditProfileDonaturRequest editProfileRequest) {
        Donatur updateDonatur = donaturService.editProfileDonatur(editProfileRequest);
        return ResponseEntity.ok(updateDonatur);
    }

    @GetMapping()
    public Page<Donatur> getAllDonatur(@RequestParam(name = "page", defaultValue = "0") int page){
        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return donaturService.getAllDonatur(pageRequest);
    }
}
