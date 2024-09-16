package com.lazis.lazissultanagung.controller;

import com.lazis.lazissultanagung.dto.request.TransactionRequest;
import com.lazis.lazissultanagung.dto.response.TransactionResponse;
import com.lazis.lazissultanagung.model.Transaction;
import com.lazis.lazissultanagung.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getTransactions(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {

        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<TransactionResponse> transactions = transactionService.getAllTransaction(month, year, pageRequest);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/{categoryType}/{id}")
    public ResponseEntity<TransactionResponse> createTransaction(@PathVariable String categoryType,
                                                                 @PathVariable Long id,
                                                                 @RequestBody TransactionRequest transactionRequest) {
        TransactionResponse transactionResponse = transactionService.createTransactionOFF(categoryType, id, transactionRequest);
        return ResponseEntity.ok(transactionResponse);
    }

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByCampaignId(
            @PathVariable Long campaignId,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<TransactionResponse> transactions = transactionService.getTransactionsByCampaignId(campaignId, pageRequest);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/zakat/{zakatId}")
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByZakatId(
            @PathVariable Long zakatId,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<TransactionResponse> transactions = transactionService.getTransactionsByZakatId(zakatId, pageRequest);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/infak/{infakId}")
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByInfakId(
            @PathVariable Long infakId,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<TransactionResponse> transactions = transactionService.getTransactionsByInfakId(infakId, pageRequest);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/dskl/{dsklId}")
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByDSKLId(
            @PathVariable Long dsklId,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<TransactionResponse> transactions = transactionService.getTransactionsByDSKLId(dsklId, pageRequest);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/wakaf/{wakafId}")
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByWakafId(
            @PathVariable Long wakafId,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        int pageSize = 12;
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<TransactionResponse> transactions = transactionService.getTransactionsByWakafId(wakafId, pageRequest);
        return ResponseEntity.ok(transactions);
    }
}
