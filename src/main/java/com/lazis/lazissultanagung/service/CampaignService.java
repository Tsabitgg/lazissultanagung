package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.dto.request.CampaignRequest;
import com.lazis.lazissultanagung.dto.response.CampaignResponse;
import com.lazis.lazissultanagung.exception.BadRequestException;
import com.lazis.lazissultanagung.model.Campaign;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CampaignService {
    CampaignResponse createCampaign(CampaignRequest campaignRequest);

    List<Campaign> getAllCampaign();

    @Transactional
    Campaign approveCampaign(Long id) throws BadRequestException;

    Page<Campaign> getCampaignByActiveAndApproved(Pageable pageable);
}
