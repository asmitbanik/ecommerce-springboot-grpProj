package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CreatePaymentRequest;
import com.example.ecommerce.dto.CreatePaymentResponse;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
import org.springframework.beans.factory.annotation.Value;

public class PaymentController {

    private final PaymentService paymentService;
    private final String razorpaySecret;

    public PaymentController(PaymentService paymentService, @Value("${razorpay.secret}") String razorpaySecret) {
        this.paymentService = paymentService;
        this.razorpaySecret = razorpaySecret;
    }

    @PostMapping("/create")
    public ResponseEntity<CreatePaymentResponse> create(@Valid @RequestBody CreatePaymentRequest req) {
        Payment p = paymentService.createPayment(req);
        String url = "https://mock-razorpay.example/pay/" + p.getProviderPaymentId();
        return ResponseEntity.ok(new CreatePaymentResponse(p.getProviderPaymentId(), url));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@RequestBody String payload, @RequestHeader(value = "X-Razorpay-Signature", required = false) String signature) {
        // verify signature
        boolean ok = true;
        if (signature != null && !signature.isBlank()) {
            ok = com.example.ecommerce.util.HmacUtil.verifyHmacSha256(payload, signature, this.razorpaySecret != null ? this.razorpaySecret : "change-me-for-webhook-validation");
        }
        if (!ok) return ResponseEntity.status(400).build();
        // parse payload into map
        try {
            com.fasterxml.jackson.databind.ObjectMapper om = new com.fasterxml.jackson.databind.ObjectMapper();
            java.util.Map<String, Object> map = om.readValue(payload, java.util.Map.class);
            paymentService.handleWebhook(map);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}