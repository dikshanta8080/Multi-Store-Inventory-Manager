package com.acharya.dikshanta.InventoryManagement.partner.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class CustomerUpdateRequest {
    private String name;
    private String contactPerson;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;
    private String address;
    private String taxId;
}
