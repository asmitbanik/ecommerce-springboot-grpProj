package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDto;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
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
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    @BeforeEach
    public void setup() {}

    @Test
    public void create_shouldSaveProductAndReturnDto() {
        ProductDto dto = new ProductDto();
        dto.setName("Test");
        dto.setPrice(BigDecimal.TEN);

        Product saved = new Product();
        saved.setId("1");
        saved.setName("Test");
        saved.setPrice(BigDecimal.TEN);

        when(productRepository.save(any(Product.class))).thenReturn(saved);

        ProductDto out = productService.create(dto);
        assertEquals("Test", out.getName());
        assertEquals(BigDecimal.TEN, out.getPrice());
    }

    @Test
    public void update_nonExisting_shouldThrow() {
        when(productRepository.findById("nope")).thenReturn(Optional.empty());
        ProductDto dto = new ProductDto();
        dto.setName("X");
        assertThrows(IllegalArgumentException.class, () -> productService.update("nope", dto));
    }
}