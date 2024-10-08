package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.dto.response.ResponseMessage;
import com.lazis.lazissultanagung.model.Coa;

import java.util.List;

public interface CoaService {
    List<Coa> getAllCoa();

    List<Coa> getCoaByAccountType(String accountType);

    Coa createCoa(Coa coa);

    Coa editCoa(Long id, Coa coa);

    ResponseMessage deleteCoa(Long id);
}
