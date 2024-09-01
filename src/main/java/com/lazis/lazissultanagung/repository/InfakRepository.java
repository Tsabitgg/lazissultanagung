package com.lazis.lazissultanagung.repository;

import com.lazis.lazissultanagung.model.Infak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfakRepository extends JpaRepository<Infak, Long> {
}
