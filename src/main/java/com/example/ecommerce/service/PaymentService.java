package com.example.ecommerce.service;

import com.example.ecommerce.dto.CreatePaymentRequest;
import com.example.ecommerce.model.Payment;

public interface PaymentService {
    Payment createPayment(CreatePaymentRequest req);
    void handleWebhook(java.util.Map<String, Object> payload);
}