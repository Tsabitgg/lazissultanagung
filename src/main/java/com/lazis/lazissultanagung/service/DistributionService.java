//package com.lazis.lazissultanagung.service;
//
//import com.lazis.lazissultanagung.dto.request.DistributionRequest;
//import com.lazis.lazissultanagung.model.Distribution;
//import org.apache.coyote.BadRequestException;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//public interface DistributionService {
//    Page<Distribution> getAllTransaction(Integer month, Integer year, Pageable pageable);
//
//    Distribution createDistribution(String categoryType, Long id, DistributionRequest distributionRequest) throws BadRequestException;
//}
