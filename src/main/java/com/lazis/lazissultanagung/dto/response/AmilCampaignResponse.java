package com.lazis.lazissultanagung.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmilCampaignResponse {
    private long campaignId;
    private String campaignName;
    private String location;
    private double targetAmount;
    private double currentAmount;
    private double amil;
    private boolean active;
}
