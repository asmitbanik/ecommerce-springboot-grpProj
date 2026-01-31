package com.example.ecommerce.service;

import com.example.ecommerce.model.ProductView;
import com.example.ecommerce.repository.PaymentRepository;
import com.example.ecommerce.repository.ProductViewRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTest {

    @Mock
    ProductViewRepository productViewRepository;

    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    AnalyticsServiceImpl analyticsService;

    @Test
    public void incrementProductView_new_shouldCreateAndSave() {
        when(productViewRepository.findById("p1")).thenReturn(Optional.empty());
        when(productViewRepository.save(any(ProductView.class))).thenAnswer(i -> i.getArgument(0));
        analyticsService.incrementProductView("p1");
        verify(productViewRepository, times(1)).save(any(ProductView.class));
    }

    @Test
    public void getProductViews_missing_shouldReturnZero() {
        when(productViewRepository.findById("p2")).thenReturn(Optional.empty());
        assertEquals(0L, analyticsService.getProductViews("p2"));
    }
}