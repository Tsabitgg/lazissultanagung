package com.lazis.lazissultanagung.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private long transactionId;

    @ManyToOne
    @JoinColumn(name = "donatur_id", referencedColumnName = "id")
    private Donatur donatur;

    @Column(length = 20)
    private String username;

    @Column(length = 15)
    private String phoneNumber;

    @Column
    private String email;

    private double transactionAmount;
    private String message;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyyMMddHHmmss")
    private LocalDateTime transactionDate;

    private String channel;
    private long vaNumber;
    private String refNo;
    private String method;

    @Column(name = "transactionQrId")
    private Long transactionQrId;
    private boolean success;

    @Column(length = 20)
    private String category;

    @ManyToOne
    @JoinColumn(name = "coa_id", referencedColumnName = "id")
    private Coa coa;

    private double debit;
    private double kredit;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "zakat_id")
    private Zakat zakat;

    @ManyToOne
    @JoinColumn(name = "infak_id")
    private Infak infak;

    @ManyToOne
    @JoinColumn(name = "dskl_id")
    private DSKL dskl;

    @ManyToOne
    @JoinColumn(name = "wakaf_id")
    private Wakaf wakaf;
}