package com.lazis.lazissultanagung.repository;

import com.lazis.lazissultanagung.model.Wakaf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WakafRepository extends JpaRepository<Wakaf, Long> {
}
