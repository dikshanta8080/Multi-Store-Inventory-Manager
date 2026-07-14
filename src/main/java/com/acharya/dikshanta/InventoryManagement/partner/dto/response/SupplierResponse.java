package com.acharya.dikshanta.InventoryManagement.partner.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SupplierResponse {
    private Long id;
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private String taxId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
