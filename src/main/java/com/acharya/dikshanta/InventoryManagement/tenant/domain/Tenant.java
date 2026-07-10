package com.acharya.dikshanta.InventoryManagement.tenant.domain;

import com.acharya.dikshanta.InventoryManagement.common.enums.TenantStatus;
import com.acharya.dikshanta.InventoryManagement.common.model.BaseEntity;
import com.acharya.dikshanta.InventoryManagement.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tenants", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String schemaName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenantStatus status;

    @OneToOne(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;
}