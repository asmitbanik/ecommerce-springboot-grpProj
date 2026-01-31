package com.example.ecommerce.service;

import com.example.ecommerce.dto.CreatePaymentRequest;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Test
    public void createPayment_shouldCreatePaymentWithProviderId() {
        CreatePaymentRequest req = new CreatePaymentRequest();
        req.setAmount(BigDecimal.valueOf(100));
        req.setOrderId("order123");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArgument(0));

        Payment p = paymentService.createPayment(req);
        assertNotNull(p.getProviderPaymentId());
        assertEquals("CREATED", p.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    public void handleWebhook_shouldUpdatePaymentStatus() {
        Payment p = new Payment();
        p.setProviderPaymentId("mock_abc");
        p.setStatus("CREATED");
        when(paymentRepository.findByProviderPaymentId("mock_abc")).thenReturn(Optional.of(p));
        paymentService.handleWebhook(java.util.Map.of("event", "payment.paid", "payment_id", "mock_abc"));
        verify(paymentRepository, times(1)).save(p);
        assertEquals("PAID", p.getStatus());
    }
}