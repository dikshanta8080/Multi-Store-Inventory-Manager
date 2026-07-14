package com.acharya.dikshanta.InventoryManagement.partner.service.impl;

import com.acharya.dikshanta.InventoryManagement.common.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.InventoryManagement.common.exceptions.BusinessException;
import com.acharya.dikshanta.InventoryManagement.partner.domain.Customer;
import com.acharya.dikshanta.InventoryManagement.partner.dto.request.CustomerCreateRequest;
import com.acharya.dikshanta.InventoryManagement.partner.dto.request.CustomerUpdateRequest;
import com.acharya.dikshanta.InventoryManagement.partner.dto.response.CustomerResponse;
import com.acharya.dikshanta.InventoryManagement.partner.mapper.CustomerMapper;
import com.acharya.dikshanta.InventoryManagement.partner.repository.CustomerRepository;
import com.acharya.dikshanta.InventoryManagement.partner.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerCreateRequest request) {
        if (customerRepository.existsByName(request.getName())) {
            throw new BusinessException("Customer with name " + request.getName() + " already exists");
        }
        if (request.getEmail() != null && customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Customer with email " + request.getEmail() + " already exists");
        }

        Customer customer = customerMapper.toEntity(request);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = getCustomer(id);
        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerUpdateRequest request) {
        Customer customer = getCustomer(id);

        if (request.getName() != null && !request.getName().equals(customer.getName()) && customerRepository.existsByName(request.getName())) {
            throw new BusinessException("Customer with name " + request.getName() + " already exists");
        }
        if (request.getEmail() != null && !request.getEmail().equals(customer.getEmail()) && customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Customer with email " + request.getEmail() + " already exists");
        }

        customerMapper.updateEntityFromRequest(request, customer);
        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    private Customer getCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
    }
}
