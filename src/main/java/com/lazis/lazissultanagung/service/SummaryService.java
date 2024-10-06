package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.dto.response.AmilCampaignResponse;
import com.lazis.lazissultanagung.dto.response.SummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SummaryService {
    SummaryResponse getSummary();

    Page<AmilCampaignResponse> getAmilCampaign(Pageable pageable);
}
