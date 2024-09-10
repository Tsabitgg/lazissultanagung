package com.lazis.lazissultanagung.repository;

import com.lazis.lazissultanagung.model.Distribution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DistributionRepository extends JpaRepository<Distribution, Long> {

    @Query("SELECT d FROM Distribution d WHERE " +
            "(:month IS NULL OR FUNCTION('MONTH', d.distributionDate) = :month) AND " +
            "(:year IS NULL OR FUNCTION('YEAR', d.distributionDate) = :year) " +
            "ORDER BY d.distributionDate DESC")
    Page<Distribution> findAllByMonthAndYear(
            @Param("month") Integer month,
            @Param("year") Integer year,
            Pageable pageable);
}
