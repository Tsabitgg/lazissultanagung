package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.dto.request.TransactionRequest;
import com.lazis.lazissultanagung.dto.response.TransactionResponse;
import com.lazis.lazissultanagung.dto.response.DonaturTransactionsHistoryResponse;
import com.lazis.lazissultanagung.exception.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {

    Page<TransactionResponse> getAllTransaction(Integer month, Integer year, Pageable pageable);

    TransactionResponse createTransactionOFF(String categoryType, Long id, TransactionRequest transactionRequest) throws BadRequestException;

    Page<TransactionResponse> getTransactionsByCampaignId(Long campaignId, Pageable pageable);

    Page<TransactionResponse> getTransactionsByZakatId(Long zakatId, Pageable pageable);

    Page<TransactionResponse> getTransactionsByInfakId(Long infakId, Pageable pageable);

    Page<TransactionResponse> getTransactionsByDSKLId(Long dsklId, Pageable pageable);

    Page<TransactionResponse> getTransactionsByWakafId(Long wakafId, Pageable pageable);

    List<DonaturTransactionsHistoryResponse> getDonaturTransactionsHistory() throws BadRequestException;
}
