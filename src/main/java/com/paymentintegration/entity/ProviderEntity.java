package com.paymentintegration.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "provider")
@Data
public class ProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_name")
    private String providerName;
}