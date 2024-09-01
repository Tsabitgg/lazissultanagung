package com.lazis.lazissultanagung.controller;

import com.lazis.lazissultanagung.exception.BadRequestException;
import com.lazis.lazissultanagung.model.Zakat;
import com.lazis.lazissultanagung.service.ZakatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("api/zakat")
public class ZakatController {

    @Autowired
    private ZakatService zakatService;

    @GetMapping
    public ResponseEntity<List<Zakat>> getAllZakat(){
        List<Zakat> zakatList = zakatService.getAllZakat();
        return ResponseEntity.ok(zakatList);
    }

    @PostMapping("/create")
    public ResponseEntity<Zakat> createZakat(@RequestBody Zakat zakat) {
        Zakat createdZakat = zakatService.crateZakat(zakat);
        return ResponseEntity.ok(createdZakat);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Zakat> updateZakat(@PathVariable Long id, @RequestBody Zakat zakat) {
        Zakat updateZakat = zakatService.updateZakat(id, zakat);
        return ResponseEntity.ok(updateZakat);
    }

}
