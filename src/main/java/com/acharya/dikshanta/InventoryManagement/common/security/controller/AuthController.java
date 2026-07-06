package com.acharya.dikshanta.InventoryManagement.common.security.controller;

import com.acharya.dikshanta.InventoryManagement.common.dto.response.ApiResponse;
import com.acharya.dikshanta.InventoryManagement.common.security.dto.AuthResponse;
import com.acharya.dikshanta.InventoryManagement.common.security.dto.LoginRequest;
import com.acharya.dikshanta.InventoryManagement.common.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Login successful", authService.login(request)));
    }
}
