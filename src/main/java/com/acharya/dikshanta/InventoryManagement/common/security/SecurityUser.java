package com.acharya.dikshanta.InventoryManagement.common.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class SecurityUser implements UserDetails {

    private final String username; // We'll use email as username
    private final String password;
    private final String tenantSchema;
    private final GrantedAuthority authority;

    public SecurityUser(String email, String password, String role, String tenantSchema) {
        this.username = email;
        this.password = password;
        this.authority = new SimpleGrantedAuthority("ROLE_" + role);
        this.tenantSchema = tenantSchema;
    }

    public String getTenantSchema() {
        return tenantSchema;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
