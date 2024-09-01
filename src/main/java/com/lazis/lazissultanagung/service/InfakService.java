package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.model.Infak;

import java.util.List;

public interface InfakService {
    List<Infak> getAllInfak();

    Infak createInfak(Infak infak);

    Infak updateInfak(Long id, Infak infak);
}
