package com.lazis.lazissultanagung.repository;

import com.lazis.lazissultanagung.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByPhoneNumber(String phoneNumber);
    Optional<Admin> findByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);

    @Query("SELECT a.username, a.email, a.phoneNumber, a.address, a.createdAt FROM Admin a WHERE a.role = 'OPERATOR' ORDER BY a.id DESC")
    Page<Object[]> getAllOperator(Pageable pageable);

}
