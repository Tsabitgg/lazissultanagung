package com.lazis.lazissultanagung.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CampaignResponse{
    private Long id;
    private String campaignCode;
    private String campaignName;
    private String categoryName;
    private String creator;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private double targetAmount;
    private double currentAmount;
    private String location;
    private boolean active;
    private boolean approved;
    private double pengajuan;
    private double realisasi;
    private double distribution;
    private boolean emergency;
}