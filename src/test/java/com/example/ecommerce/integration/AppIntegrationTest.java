package com.example.ecommerce.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppIntegrationTest {

    @Test
    public void contextLoads() {
        // Simple context load; to run full integration tests ensure MongoDB and Redis are running locally
        assert true;
    }
}