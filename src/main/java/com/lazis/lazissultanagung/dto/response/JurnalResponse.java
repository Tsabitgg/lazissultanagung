package com.lazis.lazissultanagung.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class JurnalResponse {
    private LocalDate tanggal;
    private String unit;
    private String nomorBukti;
    private String uraian;
    private String coaDebit;
    private double debit;
    private String coaKredit;
    private double kredit;
}
