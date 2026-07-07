package com.acharya.dikshanta.InventoryManagement.staff.service;

import com.acharya.dikshanta.InventoryManagement.staff.dto.request.StaffCreateRequest;
import com.acharya.dikshanta.InventoryManagement.staff.dto.response.StaffResponse;

public interface StaffService {

    StaffResponse createStaff(StaffCreateRequest request);
}
