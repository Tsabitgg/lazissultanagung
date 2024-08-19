package com.lazis.lazissultanagung.repository;

import com.lazis.lazissultanagung.model.Donatur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonaturRepository extends JpaRepository<Donatur, Long> {
    Optional<Donatur> findByPhoneNumber(String phoneNumber);
    Optional<Donatur> findByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
}