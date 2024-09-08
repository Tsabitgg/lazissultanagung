package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.dto.request.TransactionRequest;
import com.lazis.lazissultanagung.dto.response.CampaignResponse;
import com.lazis.lazissultanagung.dto.response.TransactionResponse;
import com.lazis.lazissultanagung.exception.BadRequestException;
import com.lazis.lazissultanagung.model.*;
import com.lazis.lazissultanagung.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ZakatRepository zakatRepository;

    @Autowired
    private InfakRepository infakRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private WakafRepository wakafRepository;

    @Autowired
    private DSKLRepository dsklRepository;

    @Autowired
    private DonaturRepository donaturRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<TransactionResponse> getAllTransaction(Integer month, Integer year, Pageable pageable) {
        return transactionRepository.findAllByMonthAndYear(month, year, pageable)
                .map(transaction -> {
                    Object categoryData = getCategoryData(transaction);
                    return new TransactionResponse(transaction, categoryData);
                });
    }

    @Override
    public TransactionResponse createTransactionOFF(String categoryType, Long id, TransactionRequest transactionRequest) throws BadRequestException {
        if (transactionRequest.getUsername() == null || transactionRequest.getPhoneNumber() == null) {
            throw new BadRequestException("Username atau nomor handphone tidak boleh kosong");
        }

        // Buat transaksi baru dengan data dari request
        Transaction transaction = new Transaction();
        transaction.setUsername(transactionRequest.getUsername());
        transaction.setPhoneNumber(transactionRequest.getPhoneNumber());
        transaction.setEmail(transactionRequest.getEmail());
        transaction.setTransactionAmount(transactionRequest.getTransactionAmount());
        transaction.setMessage(transactionRequest.getMessage());

        // Tentukan tipe transaksi yang diproses
        Object responseDto = null;
        switch (categoryType) {
            case "campaign":
                Campaign campaign = campaignRepository.findById(id)
                        .orElseThrow(() -> new BadRequestException("Campaign tidak ditemukan"));
                transaction.setCampaign(campaign);
                responseDto = modelMapper.map(campaign, CampaignResponse.class);
                break;
            case "zakat":
                Zakat zakat = zakatRepository.findById(id)
                        .orElseThrow(() -> new BadRequestException("Zakat tidak ditemukan"));
                transaction.setZakat(zakat);
                responseDto = modelMapper.map(zakat, Zakat.class);
                break;
            case "infak":
                Infak infak = infakRepository.findById(id)
                        .orElseThrow(() -> new BadRequestException("Infak tidak ditemukan"));
                transaction.setInfak(infak);
                responseDto = modelMapper.map(infak, Infak.class);
                break;
            case "dskl":
                DSKL dskl = dsklRepository.findById(id)
                        .orElseThrow(() -> new BadRequestException("DSKL tidak ditemukan"));
                transaction.setDskl(dskl);
                responseDto = modelMapper.map(dskl, DSKL.class);
                break;
            case "wakaf":
                Wakaf wakaf = wakafRepository.findById(id)
                        .orElseThrow(() -> new BadRequestException("Wakaf tidak ditemukan"));
                transaction.setWakaf(wakaf);
                responseDto = modelMapper.map(wakaf, Wakaf.class);
                break;
            default:
                throw new IllegalArgumentException("Invalid category type: " + categoryType);
        }

        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setCategory(categoryType);
        transaction.setMethod("OFFLINE");
        transaction.setChannel("OFFLINE");
        transaction.setVaNumber("9876547894321567");
        transaction.setSuccess(true);

        // Simpan transaksi ke database
        transaction = transactionRepository.save(transaction);

        // Update jumlah transaksi terkait berdasarkan tipe transaksi
        switch (categoryType) {
            case "campaign":
                campaignRepository.updateCampaignCurrentAmount(id, transactionRequest.getTransactionAmount());
                break;
            case "zakat":
                zakatRepository.updateZakatCurrentAmount(id, transactionRequest.getTransactionAmount());
                break;
            case "infak":
                infakRepository.updateInfakCurrentAmount(id, transactionRequest.getTransactionAmount());
                break;
            case "dskl":
                dsklRepository.updateDSKLCurrentAmount(id, transactionRequest.getTransactionAmount());
                break;
            case "wakaf":
                wakafRepository.updateWakafCurrentAmount(id, transactionRequest.getTransactionAmount());
                break;
        }

        return new TransactionResponse(transaction, responseDto);
    }

    private Object getCategoryData(Transaction transaction) {
        // Mengambil data berdasarkan kategori
        switch (transaction.getCategory()) {
            case "zakat":
                return transaction.getZakat();
            case "infak":
                return transaction.getInfak();
            case "wakaf":
                return transaction.getWakaf();
            case "dskl":
                return transaction.getDskl();
            case "campaign":
                return transaction.getCampaign();
            default:
                return null;
        }
    }

}
