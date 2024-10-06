package com.lazis.lazissultanagung.controller;

import com.lazis.lazissultanagung.dto.response.AmilCampaignResponse;
import com.lazis.lazissultanagung.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "false" )
@RestController
@RequestMapping("/api")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

//    @GetMapping("/summary")
//    public ResponseEntity<SummaryResponse> getSummary(@RequestParam(name = "year", required = false) Integer year) {
//        SummaryResponse summary = summaryService.getSummary(year);
//        return ResponseEntity.ok().body(summary);
//    }

    @GetMapping("/amil-campaign")
    public Page<AmilCampaignResponse> getAmilCampaign(@RequestParam(name = "page", defaultValue = "0") int page){
        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return summaryService.getAmilCampaign(pageRequest);
    }

//    @GetMapping("/amil-zakat")
//    public Page<AmilZakatResponse> getAmilZakat(@RequestParam(name = "page", defaultValue = "0") int page){
//        int pageSize = 6;
//        PageRequest pageRequest = PageRequest.of(page, pageSize);
//        return summaryService.getAmilZakat(pageRequest);
//    }

//    @GetMapping("/amil-infak")
//    public Page<AmilInfakResponse> getAmilInfak(@RequestParam(name = "page", defaultValue = "0") int page){
//        int pageSize = 6;
//        PageRequest pageRequest = PageRequest.of(page, pageSize);
//        return summaryService.getAmilInfak(pageRequest);
//    }

//    @GetMapping("/amil-wakaf")
//    public Page<AmilWakafResponse> getAmilWakaf(@RequestParam(name = "page", defaultValue = "0") int page){
//        int pageSize = 6;
//        PageRequest pageRequest = PageRequest.of(page, pageSize);
//        return summaryService.getAmilWakaf(pageRequest);
//    }
//
//    @GetMapping("summary-campaign")
//    public ResponseEntity<SummaryCampaignResponse> getSummaryCampaign() {
//        Optional<SummaryCampaignResponse> summary = summaryService.getSummaryCampaign();
//        return summary.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }
}