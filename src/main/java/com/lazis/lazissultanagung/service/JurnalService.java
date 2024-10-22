package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.dto.response.JurnalResponse;
import com.lazis.lazissultanagung.model.Transaction;
import com.lazis.lazissultanagung.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JurnalService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<JurnalResponse> getJurnalFilterDate(LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findAllByTransactionDateBetween(startDate, endDate);
        Map<Long, JurnalResponse> jurnalMap = new HashMap<>();

        for (Transaction transaction : transactions) {
            // Cek apakah transaksi ini sudah ada dalam ringkasan
            JurnalResponse jurnalDTO = jurnalMap.get(transaction.getTransactionId());

            if (jurnalDTO == null) {
                // Jika belum, buat entry baru
                jurnalDTO = new JurnalResponse();
                jurnalDTO.setTanggal(transaction.getTransactionDate().toLocalDate());
                jurnalDTO.setUnit("Universitas Islam Sultan Agung");
                jurnalDTO.setNomorBukti(transaction.getNomorBukti());
                jurnalDTO.setUraian(transaction.getMessage());
                jurnalMap.put(transaction.getTransactionId(), jurnalDTO);
            }

            // Cek apakah ini debit atau kredit, dan tambahkan ke COA yang sesuai
            if (transaction.getDebit() > 0) {
                jurnalDTO.setCoaDebit(transaction.getCoa().getAccountCode() + " - " + transaction.getCoa().getAccountName());
                jurnalDTO.setDebit(transaction.getDebit());
            } else if (transaction.getKredit() > 0) {
                jurnalDTO.setCoaKredit(transaction.getCoa().getAccountCode() + " - " + transaction.getCoa().getAccountName());
                jurnalDTO.setKredit(transaction.getKredit());
            }
        }

        return new ArrayList<>(jurnalMap.values());
    }

    public List<JurnalResponse> getAllJurnal() {
        List<Transaction> transactions = transactionRepository.findAll();
        Map<Long, JurnalResponse> jurnalMap = new HashMap<>();

        for (Transaction transaction : transactions) {
            // Cek apakah transaksi ini sudah ada dalam ringkasan
            JurnalResponse jurnalDTO = jurnalMap.get(transaction.getTransactionId());

            if (jurnalDTO == null) {
                // Jika belum, buat entry baru
                jurnalDTO = new JurnalResponse();
                jurnalDTO.setTanggal(transaction.getTransactionDate().toLocalDate());
                jurnalDTO.setUnit("Universitas Islam Sultan Agung");
                jurnalDTO.setNomorBukti(transaction.getNomorBukti());
                jurnalDTO.setUraian(transaction.getMessage());
                jurnalMap.put(transaction.getTransactionId(), jurnalDTO);
            }

            // Cek apakah ini debit atau kredit, dan tambahkan ke COA yang sesuai
            if (transaction.getDebit() > 0) {
                jurnalDTO.setCoaDebit(transaction.getCoa().getAccountCode() + " - " + transaction.getCoa().getAccountName());
                jurnalDTO.setDebit(transaction.getDebit());
            } else if (transaction.getKredit() > 0) {
                jurnalDTO.setCoaKredit(transaction.getCoa().getAccountCode() + " - " + transaction.getCoa().getAccountName());
                jurnalDTO.setKredit(transaction.getKredit());
            }
        }

        return new ArrayList<>(jurnalMap.values());
    }
}
