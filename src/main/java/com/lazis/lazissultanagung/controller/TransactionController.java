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
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            Pageable pageable) {

        Page<TransactionResponse> transactions = transactionService.getAllTransaction(month, year, pageable);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/{categoryType}/{id}")
    public ResponseEntity<TransactionResponse> createTransaction(@PathVariable String categoryType,
                                                                 @PathVariable Long id,
                                                                 @RequestBody TransactionRequest transactionRequest) {
        TransactionResponse transactionResponse = transactionService.createTransactionOFF(categoryType, id, transactionRequest);
        return ResponseEntity.ok(transactionResponse);
    }
}
