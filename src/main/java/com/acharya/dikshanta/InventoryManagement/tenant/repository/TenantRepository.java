package com.acharya.dikshanta.InventoryManagement.tenant.repository;

import com.acharya.dikshanta.InventoryManagement.tenant.domain.Tenant;
import com.acharya.dikshanta.InventoryManagement.common.enums.TenantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    List<Tenant> findByStatus(TenantStatus status);
}
