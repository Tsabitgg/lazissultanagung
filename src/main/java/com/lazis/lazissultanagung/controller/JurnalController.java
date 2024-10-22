package com.lazis.lazissultanagung.controller;

import com.lazis.lazissultanagung.dto.response.JurnalResponse;
import com.lazis.lazissultanagung.service.JurnalService;
import com.lazis.lazissultanagung.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("/api/journal")
public class JurnalController {

    @Autowired
    private JurnalService jurnalService;

    @GetMapping
    public ResponseEntity<List<JurnalResponse>> getJournal(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        // Jika startDate dan endDate tidak ada, ambil semua transaksi
        if (startDate == null && endDate == null) {
            return ResponseEntity.ok(jurnalService.getAllJurnal());
        }

        // Jika hanya startDate atau endDate ada, gunakan rentang yang diberikan
        List<JurnalResponse> journal = jurnalService.getJurnalFilterDate(startDate, endDate);
        return ResponseEntity.ok(journal);
    }
}

