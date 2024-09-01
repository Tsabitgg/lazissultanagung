package com.lazis.lazissultanagung.controller;

import com.lazis.lazissultanagung.dto.request.CampaignRequest;
import com.lazis.lazissultanagung.dto.response.CampaignResponse;
import com.lazis.lazissultanagung.dto.response.ResponseMessage;
import com.lazis.lazissultanagung.model.Campaign;
import com.lazis.lazissultanagung.service.CampaignService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/campaign")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @GetMapping
    public ResponseEntity<List<Campaign>> getAllCampaign(){
        List<Campaign> campaigns = campaignService.getAllCampaign();
        return ResponseEntity.ok().body(campaigns);
    }

    @PostMapping("/create")
    public ResponseEntity<CampaignResponse> createCampaign(@Valid @ModelAttribute CampaignRequest campaignRequest) {
        try {
            // Call service to create campaign
            CampaignResponse campaignResponse = campaignService.createCampaign(campaignRequest);
            return new ResponseEntity<>(campaignResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle exceptions and return appropriate response
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-active-and-approved-campaign")
    public Page<Campaign> getCampaignByActiveAndApproved(@RequestParam(name = "page", defaultValue = "0") int page){
        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return campaignService.getCampaignByActiveAndApproved(pageRequest);
    }

    @CrossOrigin
    @PutMapping("/approve-campaign")
    public ResponseMessage approveCampaign(@PathVariable Long id) {
        Campaign approvedCampaign = campaignService.approveCampaign(id);
        return new ResponseMessage(true, "Campaign Berhasil disetujui");
    }
}
