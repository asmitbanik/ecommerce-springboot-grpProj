package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentRequest {
    @NotNull
    private BigDecimal amount;

    private String currency = "INR";

    @NotNull
    private String orderId;
}