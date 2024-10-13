package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.exception.BadRequestException;
import com.lazis.lazissultanagung.model.Admin;
import com.lazis.lazissultanagung.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin getCurrentAdmin() throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return adminRepository.findByPhoneNumber(userDetails.getPhoneNumber())
                    .orElseThrow(() -> new BadRequestException("Admin/operator tidak ditemukan"));
        }
        throw new BadRequestException("Admin/operator tidak ditemukan");
    }

    @Override
    public Page<Map<String, Object>> getAllOperator(Pageable pageable) {
        Page<Object[]> results = adminRepository.getAllOperator(pageable);

        return results.map(row -> {
            String username = (String) row[0];
            String email = (String) row[1];
            String phoneNumber = (String) row[2];
            String address = (String) row[3];
            Timestamp timestamp = (Timestamp) row[4];
            LocalDateTime createdAt = timestamp.toLocalDateTime();

            Map<String, Object> operatorData = new HashMap<>();
            operatorData.put("username", username);
            operatorData.put("email", email);
            operatorData.put("phoneNumber", phoneNumber);
            operatorData.put("address", address);
            operatorData.put("createdAt", createdAt);

            return operatorData;
        });
    }

}
