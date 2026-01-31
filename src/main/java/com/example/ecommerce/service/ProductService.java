package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * Service interface that encapsulates product-related operations.
 * Implementations handle CRUD operations, listing with pagination,
 * and product image uploads.
 */
public interface ProductService {
    /**
     * Create a new product from the provided DTO.
     * @param dto product data transfer object
     * @return the created ProductDto including generated id
     */
    ProductDto create(ProductDto dto);

    /**
     * Update an existing product by id.
     * @param id product id to update
     * @param dto product data with updated fields
     * @return updated ProductDto
     * @throws IllegalArgumentException if product with given id does not exist
     */
    ProductDto update(String id, ProductDto dto);

    /**
     * Delete a product by id.
     * @param id product id to delete
     */
    void delete(String id);

    /**
     * Retrieve a product by id.
     * @param id product id
     * @return ProductDto representing the product
     * @throws IllegalArgumentException if product not found
     */
    ProductDto getById(String id);

    /**
     * List products with pagination, optional sorting and filtering.
     * @param page zero-based page index
     * @param size page size
     * @param sort sort string, e.g. "price,asc" or "name,desc"
     * @param category optional category filter (case-insensitive)
     * @param minPrice optional minimum price filter
     * @param maxPrice optional maximum price filter
     * @return a page of ProductDto
     */
    Page<ProductDto> list(int page, int size, String sort, String category, BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Upload an image for a product and store the image path on the product.
     * @param id product id
     * @param file multipart file to upload
     */
    void uploadImage(String id, MultipartFile file);
} 