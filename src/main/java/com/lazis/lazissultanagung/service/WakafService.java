package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.model.Wakaf;

import java.util.List;

public interface WakafService {
    List<Wakaf> getAllWakaf();

    Wakaf createWakaf(Wakaf wakaf);

    Wakaf updateWakaf(Long id, Wakaf wakaf);
}
