package com.example.ecommerce.controller;

import com.example.ecommerce.service.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/product-views")
    public ResponseEntity<Long> productViews(@RequestParam String productId) {
        return ResponseEntity.ok(analyticsService.getProductViews(productId));
    }

    @GetMapping("/sales")
    public ResponseEntity<BigDecimal> salesSum(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String from,
                                               @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String to) {
        Instant f = LocalDateTime.parse(from).toInstant(ZoneOffset.UTC);
        Instant t = LocalDateTime.parse(to).toInstant(ZoneOffset.UTC);
        return ResponseEntity.ok(analyticsService.getSalesSum(f, t));
    }
}