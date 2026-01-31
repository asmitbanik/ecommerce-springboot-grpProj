package com.example.ecommerce.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HmacUtilTest {

    @Test
    public void verifyHmacSha256_validSignature() throws Exception {
        String secret = "secret";
        String payload = "{\"event\":\"payment.paid\",\"payment_id\":\"mock_1\"}";
        java.lang.String sig;
        {
            javax.crypto.Mac sha256_HMAC = javax.crypto.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec secret_key = new javax.crypto.spec.SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(payload.getBytes());
            sig = java.util.HexFormat.of().formatHex(hash);
        }
        assertTrue(HmacUtil.verifyHmacSha256(payload, sig, secret));
    }

    @Test
    public void verifyHmacSha256_invalidSignature() {
        assertFalse(HmacUtil.verifyHmacSha256("payload", "bad", "secret"));
    }
}