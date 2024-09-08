package com.lazis.lazissultanagung.repository;

import com.lazis.lazissultanagung.model.Zakat;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ZakatRepository extends JpaRepository<Zakat, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Zakat z SET z.amount = z.amount + :transactionAmount WHERE z.id = :id")
    void updateZakatCurrentAmount(@Param("id") Long id, @Param("transactionAmount") double transactionAmount);
}
