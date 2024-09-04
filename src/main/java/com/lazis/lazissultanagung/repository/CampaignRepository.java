package com.lazis.lazissultanagung.repository;

import com.lazis.lazissultanagung.dto.response.CampaignResponse;
import com.lazis.lazissultanagung.model.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    @Query("SELECT c FROM Campaign c WHERE c.campaignCategory.campaignCategory = :campaignCategory " +
            "AND c.active = true " +
            "AND c.approved = true ORDER BY c.campaignId DESC")
    Page<Campaign> findByCategoryName(@Param("campaignCategory") String campaignCategory, Pageable pageable);


//    List<Campaign> findByApproved(boolean approved);
//    List<Campaign> findCampaignByActive(boolean isActive);

    @Query("SELECT c FROM Campaign c WHERE c.active = true AND c.approved = true ORDER BY c.campaignId DESC")
    Page<Campaign> findCampaignByActiveAndApproved(Pageable pageable);

    @Query("SELECT c FROM Campaign c WHERE c.active = true AND c.approved = true AND c.emergency = true ORDER BY c.campaignId DESC")
    Page<Campaign> findCampaignByEmergency(Pageable pageable);

//
//    @Query("SELECT c FROM Campaign c WHERE c.active = false AND c.approved = true ORDER BY c.campaignId DESC")
//    Page<Campaign> findHistoryCampaign(Pageable pageable);
//
//    Campaign findByCampaignCode(String campaignCode);
//
//    Campaign findById(long campaignId);
//
    @Query("SELECT c FROM Campaign c WHERE LOWER(c.campaignName) LIKE LOWER(CONCAT('%', :campaignName, '%')) AND c.approved = true AND c.active = true")
    Page<Campaign> findByCampaignName(@Param("campaignName") String campaignName, Pageable pageable);
//
//    @Query("SELECT c FROM Campaign c WHERE YEAR(c.startDate) = :year")
//    Page<Campaign> findByYear(@Param("year") int year, Pageable pageable);
//
//    @Query("SELECT \n" +
//            " c.campaignId,\n" +
//            " c.campaignName,\n" +
//            " c.location,\n" +
//            " c.targetAmount,\n" +
//            " c.currentAmount,\n" +
//            " c.currentAmount * 0.15 AS amil,\n" +
//            " c.active\n" +
//            "FROM \n" +
//            " Campaign c\n" +
//            "GROUP BY c.campaignId, c.currentAmount \n" +
//            "ORDER BY c.campaignId DESC"
//    )
//    Page<Object []> getAmilCampaign(Pageable pageable);
//
//    @Query("SELECT SUM(c.currentAmount) AS totalCampaignTransactionAmount, " +
//            "SUM(c.currentAmount * 0.15) AS totalAmil, " +
//            "SUM(c.distribution) AS totalCampaignDistributionAmount " +
//            "FROM Campaign c")
//    Optional<Map<String, Double>> getSummaryCampaign();
//
//    @Query("SELECT c FROM Campaign c WHERE c.creator IN (SELECT sa FROM SubAdmin sa WHERE sa.serviceOffice.serviceOfficeId = :serviceOfficeId) ORDER BY c.campaignId DESC")
//    Page<Campaign> findCampaignsByServiceOfficeId(@Param("serviceOfficeId") long serviceOfficeId, Pageable pageable);
//
//    Page<Campaign> findAllByApprovedIsTrue(Pageable pageable);

}
