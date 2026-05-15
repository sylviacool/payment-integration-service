package com.paymentintegration.repository;

import com.paymentintegration.entity.TransactionStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionStatusRepository
        extends JpaRepository<TransactionStatusEntity, Long> {

    Optional<TransactionStatusEntity> findByName(String name);

}