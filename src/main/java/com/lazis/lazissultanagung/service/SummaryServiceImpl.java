package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.dto.response.AmilCampaignResponse;
import com.lazis.lazissultanagung.dto.response.AmilZiswafResponse;
import com.lazis.lazissultanagung.dto.response.SummaryResponse;
import com.lazis.lazissultanagung.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    private DistributionRepository distributionRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private DonaturRepository donaturRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ZakatRepository zakatRepository;

    @Autowired
    private InfakRepository infakRepository;

    @Autowired
    private WakafRepository wakafRepository;

    @Autowired
    private DSKLRepository dsklRepository;

    @Override
    public SummaryResponse getSummary() {
        SummaryResponse summary = new SummaryResponse();
            summary.setTotalDistributionAmount(distributionRepository.totalDistributionAmount());
            summary.setTotalDistributionReceiver(distributionRepository.totalDistributionReceiver());
            summary.setTotalTransactionAmount(transactionRepository.totalTransactionAmount());
            summary.setTotalDonatur(transactionRepository.getTotalDonatur());
        return summary;
    }

    @Override
    public Page<Object> getAmilByCategory(String category, Pageable pageable) {
        List<Object> response = new ArrayList<>();
        Page<Object> resultPage;

        switch (category.toLowerCase()) {
            case "zakat":
                resultPage = zakatRepository.findAll(pageable).map(zakat ->
                        new AmilZiswafResponse(
                                zakat.getId(),
                                "zakat",
                                zakat.getCategoryName(),
                                zakat.getAmount(),
                                zakat.getAmount() * 0.125
                        )
                );
                break;

            case "infak":
                resultPage = infakRepository.findAll(pageable).map(infak ->
                        new AmilZiswafResponse(
                                infak.getId(),
                                "infak",
                                infak.getCategoryName(),
                                infak.getAmount(),
                                infak.getAmount() * 0.125
                        )
                );
                break;

            case "wakaf":
                resultPage = wakafRepository.findAll(pageable).map(wakaf ->
                        new AmilZiswafResponse(
                                wakaf.getId(),
                                "wakaf",
                                wakaf.getCategoryName(),
                                wakaf.getAmount(),
                                wakaf.getAmount() * 0.10
                        )
                );
                break;

            case "dskl":
                resultPage = dsklRepository.findAll(pageable).map(dskl ->
                        new AmilZiswafResponse(
                                dskl.getId(),
                                "dskl",
                                dskl.getCategoryName(),
                                dskl.getAmount(),
                                dskl.getAmount() * 0.125
                        )
                );
                break;

            case "campaign":
                resultPage = campaignRepository.findAll(pageable).map(campaign ->
                        new AmilCampaignResponse(
                                campaign.getCampaignId(),
                                campaign.getCampaignName(),
                                campaign.getLocation(),
                                campaign.getTargetAmount(),
                                campaign.getCurrentAmount(),
                                campaign.getCurrentAmount() * 0.125,
                                campaign.isActive()
                        )
                );
                break;

            default:
                throw new IllegalArgumentException("Invalid category: " + category);
        }

        return resultPage;
    }
//
//    @Override
//    public Optional<SummaryCampaignResponse> getSummaryCampaign() {
//        Optional<Map<String, Double>> summaryMap = campaignRepository.getSummaryCampaign();
//        return summaryMap.map(map -> new SummaryCampaignResponse(
//                map.getOrDefault("totalCampaignTransactionAmount", 0.0),
//                map.getOrDefault("totalAmil", 0.0),
//                map.getOrDefault("totalCampaignDistributionAmount", 0.0)));
//    }
}
