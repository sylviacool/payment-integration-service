package com.paymentintegration.service.impl;

import com.paymentintegration.entity.TransactionEntity;
import com.paymentintegration.repository.TransactionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldSaveTransactionSuccessfully() {

        TransactionEntity transaction = new TransactionEntity();
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setCurrency("USD");

        when(transactionRepository.save(any(TransactionEntity.class
        ))).thenReturn(transaction);

        TransactionEntity savedTransaction = transactionRepository.save(transaction);

        assertNotNull(savedTransaction);
        assertEquals("USD", savedTransaction.getCurrency());

        verify(transactionRepository)
                .save(transaction);
    }
}