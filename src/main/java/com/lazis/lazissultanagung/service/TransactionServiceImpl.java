package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.dto.request.TransactionRequest;
import com.lazis.lazissultanagung.dto.response.CampaignResponse;
import com.lazis.lazissultanagung.dto.response.TransactionResponse;
import com.lazis.lazissultanagung.dto.response.DonaturTransactionsHistoryResponse;
import com.lazis.lazissultanagung.exception.BadRequestException;
import com.lazis.lazissultanagung.model.*;
import com.lazis.lazissultanagung.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private MessagesRepository messagesRepository;

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

        Transaction transaction = new Transaction();
        transaction.setUsername(transactionRequest.getUsername());
        transaction.setPhoneNumber(transactionRequest.getPhoneNumber());
        transaction.setEmail(transactionRequest.getEmail());
        transaction.setTransactionAmount(transactionRequest.getTransactionAmount());
        transaction.setMessage(transactionRequest.getMessage());

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

        transaction = transactionRepository.save(transaction);

        switch (categoryType) {
            case "campaign":
                campaignRepository.updateCampaignCurrentAmount(id, transactionRequest.getTransactionAmount());

                Messages messages = new Messages();
                messages.setUsername(transaction.getUsername());
                messages.setMessagesDate(transaction.getTransactionDate());
                messages.setMessages(transaction.getMessage());
                messages.setCampaign(transaction.getCampaign());
                messages.setAamiin(messages.getAamiin() +1);
                messagesRepository.save(messages);

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

    @Override
    public Page<TransactionResponse> getTransactionsByCampaignId(Long campaignId, Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findByCampaignId(campaignId, pageable);

        return transactions.map(transaction -> new TransactionResponse(transaction, getCategoryData(transaction)));
    }

    @Override
    public Page<TransactionResponse> getTransactionsByZakatId(Long zakatId, Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findByZakatId(zakatId, pageable);

        return transactions.map(transaction -> new TransactionResponse(transaction, getCategoryData(transaction)));
    }

    @Override
    public Page<TransactionResponse> getTransactionsByInfakId(Long infakId, Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findByInfakId(infakId, pageable);

        return transactions.map(transaction -> new TransactionResponse(transaction, getCategoryData(transaction)));
    }

    @Override
    public Page<TransactionResponse> getTransactionsByDSKLId(Long dsklId, Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findByDSKLId(dsklId, pageable);

        return transactions.map(transaction -> new TransactionResponse(transaction, getCategoryData(transaction)));
    }

    @Override
    public Page<TransactionResponse> getTransactionsByWakafId(Long wakafId, Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findByWakafId(wakafId, pageable);

        return transactions.map(transaction -> new TransactionResponse(transaction, getCategoryData(transaction)));
    }

    @Override
    public List<DonaturTransactionsHistoryResponse> getDonaturTransactionsHistory() throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Ambil nomor telepon dari user yang login
            String phoneNumber = userDetails.getPhoneNumber();

            // Cari transaksi berdasarkan nomor telepon
            List<Transaction> donaturTransactions = transactionRepository.findByPhoneNumber(phoneNumber);

            // Konversi transaksi ke DTO
            List<DonaturTransactionsHistoryResponse> donaturTransactionsHistory = new ArrayList<>();
            for (Transaction transaction : donaturTransactions) {
                DonaturTransactionsHistoryResponse transactionDTO = new DonaturTransactionsHistoryResponse();
                transactionDTO.setUsername(transaction.getUsername());
                transactionDTO.setTransactionAmount(transaction.getTransactionAmount());
                transactionDTO.setMessage(transaction.getMessage());
                transactionDTO.setTransactionDate(transaction.getTransactionDate());
                transactionDTO.setSuccess(transaction.isSuccess());

                // Tentukan tipe transaksi dan nama transaksinya
                Object categoryData = getCategoryData(transaction);
                if (categoryData != null) {
                    if (categoryData instanceof Campaign) {
                        transactionDTO.setCategory("Campaign");
                        transactionDTO.setTransactionName(((Campaign) categoryData).getCampaignName());
                    } else if (categoryData instanceof Zakat) {
                        transactionDTO.setCategory("Zakat");
                        transactionDTO.setTransactionName(((Zakat) categoryData).getCategoryName());
                    } else if (categoryData instanceof Infak) {
                        transactionDTO.setCategory("Infak");
                        transactionDTO.setTransactionName(((Infak) categoryData).getCategoryName());
                    } else if (categoryData instanceof Wakaf) {
                        transactionDTO.setCategory("Wakaf");
                        transactionDTO.setTransactionName(((Wakaf) categoryData).getCategoryName());
                    }
                }

                donaturTransactionsHistory.add(transactionDTO);
            }

            return donaturTransactionsHistory;
        }

        throw new BadRequestException("Donatur tidak ditemukan");
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
