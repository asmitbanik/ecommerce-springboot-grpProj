package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface ProductService {
    ProductDto create(ProductDto dto);
    ProductDto update(String id, ProductDto dto);
    void delete(String id);
    ProductDto getById(String id);
    Page<ProductDto> list(int page, int size, String sort, String category, BigDecimal minPrice, BigDecimal maxPrice);
    void uploadImage(String id, MultipartFile file);
}