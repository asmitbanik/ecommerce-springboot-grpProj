package com.example.ecommerce.service;

import com.example.ecommerce.dto.CreatePaymentRequest;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment createPayment(CreatePaymentRequest req) {
        Payment p = new Payment();
        p.setOrderId(req.getOrderId());
        p.setAmount(req.getAmount());
        p.setCurrency(req.getCurrency());
        p.setStatus("CREATED");
        p.setProviderPaymentId("mock_" + UUID.randomUUID());
        return paymentRepository.save(p);
    }

    @Override
    public void handleWebhook(Map<String, Object> payload) {
        // expected payload: {"event":"payment.paid", "payment_id":"mock_xxx"}
        String event = (String) payload.get("event");
        String paymentId = (String) payload.get("payment_id");
        if (event == null || paymentId == null) return;
        paymentRepository.findByProviderPaymentId(paymentId).ifPresent(p -> {
            if ("payment.paid".equals(event)) {
                p.setStatus("PAID");
            } else if ("payment.failed".equals(event)) {
                p.setStatus("FAILED");
            }
            paymentRepository.save(p);
        });
    }
}