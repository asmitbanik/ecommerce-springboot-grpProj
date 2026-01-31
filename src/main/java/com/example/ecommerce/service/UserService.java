package com.example.ecommerce.service;

import com.example.ecommerce.dto.AuthResponse;
import com.example.ecommerce.dto.AuthRequest;
import com.example.ecommerce.dto.RegisterRequest;

public interface UserService {
    AuthResponse login(AuthRequest request);
    void register(RegisterRequest request);
}