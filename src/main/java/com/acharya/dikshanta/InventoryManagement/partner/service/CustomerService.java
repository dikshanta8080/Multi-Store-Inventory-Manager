package com.acharya.dikshanta.InventoryManagement.partner.service;

import com.acharya.dikshanta.InventoryManagement.partner.dto.request.CustomerCreateRequest;
import com.acharya.dikshanta.InventoryManagement.partner.dto.request.CustomerUpdateRequest;
import com.acharya.dikshanta.InventoryManagement.partner.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerCreateRequest request);
    CustomerResponse getCustomerById(Long id);
    List<CustomerResponse> getAllCustomers();
    CustomerResponse updateCustomer(Long id, CustomerUpdateRequest request);
    void deleteCustomer(Long id);
}
