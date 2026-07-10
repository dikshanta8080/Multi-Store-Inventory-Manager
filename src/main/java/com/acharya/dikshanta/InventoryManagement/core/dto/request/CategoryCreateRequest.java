package com.acharya.dikshanta.InventoryManagement.core.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(
        @NotBlank(message = "name is required") String name,
        @NotBlank(message = "description is required") String description
) {
}
