package com.acharya.dikshanta.InventoryManagement.staff.controller;

import com.acharya.dikshanta.InventoryManagement.common.dto.response.ApiResponse;
import com.acharya.dikshanta.InventoryManagement.staff.dto.request.StaffCreateRequest;
import com.acharya.dikshanta.InventoryManagement.staff.dto.response.StaffResponse;
import com.acharya.dikshanta.InventoryManagement.staff.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    // Only Tenant Admin or Manager can create staff
    @PostMapping
    @PreAuthorize("hasAnyRole('TENANT_ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<StaffResponse>> createStaff(@RequestBody @Valid StaffCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Staff created successfully", staffService.createStaff(request)));
    }
}
