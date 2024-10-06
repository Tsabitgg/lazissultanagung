package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.dto.response.AmilCampaignResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SummaryService {
    Page<AmilCampaignResponse> getAmilCampaign(Pageable pageable);
}
