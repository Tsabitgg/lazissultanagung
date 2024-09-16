package com.lazis.lazissultanagung.repository;

import com.lazis.lazissultanagung.model.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE " +
            "(:month IS NULL OR FUNCTION('MONTH', t.transactionDate) = :month) AND " +
            "(:year IS NULL OR FUNCTION('YEAR', t.transactionDate) = :year) " +
            "ORDER BY t.transactionDate DESC")
    Page<Transaction> findAllByMonthAndYear(
            @Param("month") Integer month,
            @Param("year") Integer year,
            Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.category = 'campaign' " +
            "AND t.campaign.campaignId = :campaignId ORDER BY t.transactionDate DESC ")
    Page<Transaction> findByCampaignId(@Param("campaignId") Long campaignId, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.category = 'zakat' " +
            "AND t.zakat.id = :zakatId ORDER BY t.transactionDate DESC ")
    Page<Transaction> findByZakatId(@Param("zakatId") Long zakatId, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.category = 'infak' " +
            "AND t.infak.id = :infakId ORDER BY t.transactionDate DESC ")
    Page<Transaction> findByInfakId(@Param("infakId") Long infakId, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.category = 'dskl' " +
            "AND t.dskl.id = :dsklId ORDER BY t.transactionDate DESC ")
    Page<Transaction> findByDSKLId(@Param("dsklId") Long dsklId, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.category = 'wakaf' " +
            "AND t.wakaf.id = :wakafId ORDER BY t.transactionDate DESC ")
    Page<Transaction> findByWakafId(@Param("wakafId") Long wakafId, Pageable pageable);

}
