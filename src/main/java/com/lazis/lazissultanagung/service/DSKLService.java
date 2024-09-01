package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.model.DSKL;

import java.util.List;

public interface DSKLService {
    List<DSKL> getAllDSKL();

    DSKL createDSKL(DSKL dskl);

    DSKL updateDSKL(Long id, DSKL dskl);

    void deleteDSKL(Long id);
}
