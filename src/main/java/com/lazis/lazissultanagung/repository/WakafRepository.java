package com.lazis.lazissultanagung.repository;

import com.lazis.lazissultanagung.model.Wakaf;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WakafRepository extends JpaRepository<Wakaf, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Wakaf w SET w.amount = w.amount + :transactionAmount WHERE w.id = :id")
    void updateWakafCurrentAmount(@Param("id") Long id, @Param("transactionAmount") double transactionAmount);

}
