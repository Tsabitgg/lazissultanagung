package com.lazis.lazissultanagung.controller;

import com.lazis.lazissultanagung.model.DSKL;
import com.lazis.lazissultanagung.service.DSKLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("api/dskl")
public class DSKLController {

    @Autowired
    private DSKLService dsklService;

    @GetMapping
    public ResponseEntity<List<DSKL>> getAllDSKL(){
        List<DSKL> dsklList = dsklService.getAllDSKL();
        return ResponseEntity.ok(dsklList);
    }

    @PostMapping("/create")
    public ResponseEntity<DSKL> createDSKL(@RequestBody DSKL dskl) {
        DSKL createDSKL = dsklService.createDSKL(dskl);
        return ResponseEntity.ok(dskl);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DSKL> updateDSKL(@PathVariable Long id, @RequestBody DSKL dskl){
        DSKL updateDSKL = dsklService.updateDSKL(id, dskl);
        return ResponseEntity.ok(updateDSKL);
    }

}
