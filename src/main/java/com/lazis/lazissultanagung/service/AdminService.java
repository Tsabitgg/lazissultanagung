package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.exception.BadRequestException;
import com.lazis.lazissultanagung.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface AdminService {
    Admin getCurrentAdmin() throws BadRequestException;

    Page<Map<String, Object>> getAllOperator(Pageable pageable);
}
