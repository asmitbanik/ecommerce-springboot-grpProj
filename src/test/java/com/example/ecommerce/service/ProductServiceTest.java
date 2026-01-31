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
/**
 * Unit tests for {@link ProductServiceImpl} covering basic create and update
 * behavior. Tests use Mockito to stub repository interactions and verify
 * expected exceptions and DTO conversions.
 */
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    @BeforeEach
    public void setup() {}

    /**
     * Verifies that creating a product results in a persisted entity being
     * translated back to a ProductDto with the expected values.
     */
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

    /**
     * Ensures that updating a non-existing product throws IllegalArgumentException.
     */
    @Test
    public void update_nonExisting_shouldThrow() {
        when(productRepository.findById("nope")).thenReturn(Optional.empty());
        ProductDto dto = new ProductDto();
        dto.setName("X");
        assertThrows(IllegalArgumentException.class, () -> productService.update("nope", dto));
    }
} 