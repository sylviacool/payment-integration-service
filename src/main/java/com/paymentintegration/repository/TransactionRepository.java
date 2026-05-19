package com.paymentintegration.repository;

import com.paymentintegration.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<TransactionEntity, Long> {

    Optional<TransactionEntity> findByTxnReference(String txnReference);

    Optional<TransactionEntity> findByProviderReference(String providerReference);


}