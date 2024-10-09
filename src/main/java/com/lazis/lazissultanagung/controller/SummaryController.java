package com.lazis.lazissultanagung.controller;

import com.lazis.lazissultanagung.dto.response.AmilCampaignResponse;
import com.lazis.lazissultanagung.dto.response.SummaryResponse;
import com.lazis.lazissultanagung.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/summary")
    public ResponseEntity<SummaryResponse> getSummary() {
        SummaryResponse summary = summaryService.getSummary();
        return ResponseEntity.ok().body(summary);
    }

    @GetMapping("/amil/{category}")
    public ResponseEntity<Page<Object>> getAmilByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page
    ) {
        int pageSize = 12;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Object> response = summaryService.getAmilByCategory(category, pageable);
        return ResponseEntity.ok(response);
    }


//    @GetMapping("summary-campaign")
//    public ResponseEntity<SummaryCampaignResponse> getSummaryCampaign() {
//        Optional<SummaryCampaignResponse> summary = summaryService.getSummaryCampaign();
//        return summary.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }
}