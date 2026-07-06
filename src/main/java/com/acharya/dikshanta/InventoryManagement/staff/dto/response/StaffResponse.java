package com.acharya.dikshanta.InventoryManagement.staff.dto.response;

import lombok.Builder;

@Builder
public record StaffResponse(
        Long id,
        String name,
        String email,
        String position,
        String role,
        String tenantName
) {}
