package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.dto.response.AmilCampaignResponse;
import com.lazis.lazissultanagung.dto.response.SummaryResponse;
import com.lazis.lazissultanagung.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public Page<AmilCampaignResponse> getAmilCampaign(Pageable pageable) {
        Page<Object[]> results = campaignRepository.getAmilCampaign(pageable);
        return results.map(result -> new AmilCampaignResponse(
                (Long) result[0],
                (String) result[1],
                (String) result[2],
                (Double) result[3],
                (Double) result[4],
                (Double) result[5],
                (Boolean) result[6]
        ));
    }

//    @Override
//    public Page<AmilZiswafResponse> getAmilZakat(Pageable pageable) {
//        Page<Object[]> results = zakatRepository.getAmilZakat(pageable);
//        return results.map(result -> new AmilZiswafResponse(
//                (Long) result[0],
//                (ZakatCategory) result[1],
//                (String) result[2],
//                (Double) result[3],
//                (Double) result[4]
//        ));
//    }
//
//    @Override
//    public Page<AmilZiswafResponse> getAmilInfak(Pageable pageable) {
//        Page<Object[]> results = infakRepository.getAmilInfak(pageable);
//        return results.map(result -> new AmilZiswafResponse(
//                (Long) result[0],
//                (InfakCategory) result[1],
//                (String) result[2],
//                (Double) result[3],
//                (Double) result[4]
//        ));
//    }
//
//    @Override
//    public Page<AmilZiswafResponse> getAmilWakaf(Pageable pageable) {
//        Page<Object[]> results = wakafRepository.getAmilWakaf(pageable);
//        return results.map(result -> new AmilZiswafResponse(
//                (Long) result[0],
//                (WakafCategory) result[1],
//                (String) result[2],
//                (Double) result[3],
//                (Double) result[4]
//        ));
//    }
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
