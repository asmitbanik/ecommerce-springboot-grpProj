package com.example.ecommerce.service;

import java.time.Instant;

public interface AnalyticsService {
    void incrementProductView(String productId);
    long getProductViews(String productId);
    java.math.BigDecimal getSalesSum(Instant from, Instant to);
}