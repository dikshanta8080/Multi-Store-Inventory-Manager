package com.acharya.dikshanta.InventoryManagement.staff.dto.request;

import com.acharya.dikshanta.InventoryManagement.staff.domain.StaffRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record StaffCreateRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Position is required")
        String position,

        @NotNull(message = "Role is required")
        StaffRole role
) {}
