package com.paymentintegration.repository;

import com.paymentintegration.entity.ProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepository
        extends JpaRepository<ProviderEntity, Long> {

    Optional<ProviderEntity> findByProviderName(String providerName);

}