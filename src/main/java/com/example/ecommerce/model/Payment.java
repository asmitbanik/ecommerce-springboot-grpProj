package com.example.ecommerce.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private String orderId;
    private BigDecimal amount;
    private String currency = "INR";
    private String status; // CREATED, PAID, FAILED
    private String providerPaymentId;
    private Instant createdAt = Instant.now();
}