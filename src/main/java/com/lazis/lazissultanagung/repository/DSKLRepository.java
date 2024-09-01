package com.lazis.lazissultanagung.repository;

import com.lazis.lazissultanagung.model.DSKL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DSKLRepository extends JpaRepository<DSKL, Long> {
}
