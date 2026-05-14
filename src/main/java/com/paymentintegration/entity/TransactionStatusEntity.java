package com.paymentintegration.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "transaction_status")
@Data
public class TransactionStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}