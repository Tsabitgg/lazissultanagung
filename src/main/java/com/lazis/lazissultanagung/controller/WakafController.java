package com.lazis.lazissultanagung.controller;

import com.lazis.lazissultanagung.model.Infak;
import com.lazis.lazissultanagung.model.Wakaf;
import com.lazis.lazissultanagung.model.Zakat;
import com.lazis.lazissultanagung.service.WakafService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("api/wakaf")
public class WakafController {

    @Autowired
    private WakafService wakafService;

    @GetMapping
    public ResponseEntity<List<Wakaf>> getAllWakaf(){
        List<Wakaf> wakafList = wakafService.getAllWakaf();
        return ResponseEntity.ok(wakafList);
    }

    @PostMapping("/create")
    public ResponseEntity<Wakaf> createWakaf(@RequestBody Wakaf wakaf) {
        Wakaf createWakaf = wakafService.createWakaf(wakaf);
        return ResponseEntity.ok(createWakaf);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Wakaf> updateInfak(@PathVariable Long id, @RequestBody Wakaf wakaf) {
        Wakaf updateWakaf = wakafService.updateWakaf(id, wakaf);
        return ResponseEntity.ok(updateWakaf);
    }

}
