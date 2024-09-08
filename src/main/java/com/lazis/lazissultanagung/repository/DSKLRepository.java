package com.lazis.lazissultanagung.repository;

import com.lazis.lazissultanagung.model.DSKL;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DSKLRepository extends JpaRepository<DSKL, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE DSKL d SET d.amount = d.amount + :transactionAmount WHERE d.id = :id")
    void updateDSKLCurrentAmount(@Param("id") Long id, @Param("transactionAmount") double transactionAmount);
}
