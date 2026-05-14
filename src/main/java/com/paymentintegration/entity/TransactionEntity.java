package com.paymentintegration.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_table")
@Data
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private BigDecimal amount;

    private String currency;

    private String providerReference;

    private String merchantTransactionReference;

    private String txnReference;

    private String errorCode;

    private String errorMessage;

    private Integer retryCount;

    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private ProviderEntity provider;

    @ManyToOne
    @JoinColumn(name = "txn_status_id")
    private TransactionStatusEntity transactionStatus;
}