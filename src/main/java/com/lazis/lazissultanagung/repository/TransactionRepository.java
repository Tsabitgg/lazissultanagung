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

}
