package com.lazis.lazissultanagung.repository;

import com.lazis.lazissultanagung.model.Zakat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZakatRepository extends JpaRepository<Zakat, Long> {
}
