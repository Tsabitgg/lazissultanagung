package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.exception.BadRequestException;
import com.lazis.lazissultanagung.model.Zakat;

import java.util.List;


public interface ZakatService {
    List<Zakat> getAllZakat();

    Zakat crateZakat(Zakat zakat) throws BadRequestException;
}
