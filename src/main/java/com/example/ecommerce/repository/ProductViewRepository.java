package com.example.ecommerce.repository;

import com.example.ecommerce.model.ProductView;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductViewRepository extends MongoRepository<ProductView, String> {
}