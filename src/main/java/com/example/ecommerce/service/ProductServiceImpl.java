package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDto;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.cache.annotation.CacheEvict;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
/**
 * Default ProductService implementation that uses a JPA repository to
 * perform CRUD operations and supports pagination, sorting, caching,
 * and image uploads. Cache entries for product listings are evicted on
 * create/update/delete operations to keep results fresh.
 */
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final String uploadDir;

    public ProductServiceImpl(ProductRepository productRepository, @Value("${file.upload-dir}") String uploadDir) {
        this.productRepository = productRepository;
        this.uploadDir = uploadDir;
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    /**
     * Create and persist a new product.
     * Cache for product listings is cleared when a new product is created.
     */
    public ProductDto create(ProductDto dto) {
        Product p = new Product();
        BeanUtils.copyProperties(dto, p);
        Product saved = productRepository.save(p);
        ProductDto out = new ProductDto();
        BeanUtils.copyProperties(saved, out);
        return out;
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    /**
     * Update an existing product. Throws IllegalArgumentException if the product
     * does not exist.
     */
    public ProductDto update(String id, ProductDto dto) {
        Product p = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setCategory(dto.getCategory());
        Product saved = productRepository.save(p);
        ProductDto out = new ProductDto();
        BeanUtils.copyProperties(saved, out);
        return out;
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    /**
     * Delete a product by id. If the id is not found, the repository implementation
     * will handle the behavior (may be no-op or thrown exception).
     */
    public void delete(String id) {
        productRepository.deleteById(id);
    }

    @Override
    /**
     * Fetch a product by id and convert to DTO.
     */
    public ProductDto getById(String id) {
        Product p = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        ProductDto out = new ProductDto();
        BeanUtils.copyProperties(p, out);
        return out;
    }

    @Override
    @Cacheable(value = "products", key = "#page + '-' + #size + '-' + #sort + '-' + #category + '-' + #minPrice + '-' + #maxPrice")
    /**
     * List products using pageable parameters. Results are cached to improve
     * performance. Cache key includes pagination, sort and filter criteria.
     */
    public Page<ProductDto> list(int page, int size, String sort, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        Sort s = Sort.unsorted();
        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            s = Sort.by(Sort.Direction.fromString(parts[1]), parts[0]);
        }
        Pageable pageable = PageRequest.of(page, size, s);
        Page<Product> pg;
        if (category != null && !category.isBlank()) {
            pg = productRepository.findByCategoryIgnoreCase(category, pageable);
        } else {
            pg = productRepository.findAll(pageable);
        }
        List<ProductDto> dtos = pg.getContent().stream().map(p -> {
            ProductDto d = new ProductDto();
            BeanUtils.copyProperties(p, d);
            return d;
        }).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, pg.getTotalElements());
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    /**
     * Save an uploaded image to the configured upload directory and persist
     * the file path on the product entity.
     */
    public void uploadImage(String id, MultipartFile file) {
        Product p = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();
        String filename = id + "-" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        File dest = new File(dir, filename);
        try {
            file.transferTo(dest);
            p.setImagePath(dest.getAbsolutePath());
            productRepository.save(p);
        } catch (IOException e) {
            throw new RuntimeException("File save failed", e);
        }
    }
} 