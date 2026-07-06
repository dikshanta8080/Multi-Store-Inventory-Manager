package com.acharya.dikshanta.InventoryManagement.tenant.repository;

import com.acharya.dikshanta.InventoryManagement.common.enums.TenantStatus;
import com.acharya.dikshanta.InventoryManagement.tenant.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    List<Tenant> findByStatus(TenantStatus status);

    Optional<Tenant> findByName(String name);

    boolean existsByName(String name);


    Optional<Tenant> findBySchemaName(String email);
}
