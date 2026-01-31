package com.example.ecommerce.controller;

import com.example.ecommerce.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class PaymentControllerTest {

    @Mock
    PaymentService paymentService;

    public PaymentControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void webhook_validSignature_shouldCallService() throws Exception {
        String secret = "secret";
        com.example.ecommerce.controller.PaymentController controller = new com.example.ecommerce.controller.PaymentController(paymentService, secret);
        String payload = "{\"event\":\"payment.paid\",\"payment_id\":\"mock_1\"}";
        // compute signature
        javax.crypto.Mac sha256_HMAC = javax.crypto.Mac.getInstance("HmacSHA256");
        javax.crypto.spec.SecretKeySpec secret_key = new javax.crypto.spec.SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] hash = sha256_HMAC.doFinal(payload.getBytes());
        String sig = java.util.HexFormat.of().formatHex(hash);

        var resp = controller.webhook(payload, sig);
        assert(resp.getStatusCode().is2xxSuccessful());
        verify(paymentService, times(1)).handleWebhook(any());
    }

    @Test
    public void webhook_invalidSignature_shouldReturnBadRequest() {
        String secret = "secret";
        com.example.ecommerce.controller.PaymentController controller = new com.example.ecommerce.controller.PaymentController(paymentService, secret);
        String payload = "{}";
        var resp = controller.webhook(payload, "bad");
        assert(resp.getStatusCode().is4xxClientError());
        verify(paymentService, never()).handleWebhook(any());
    }
}