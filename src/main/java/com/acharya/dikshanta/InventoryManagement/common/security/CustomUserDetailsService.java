package com.acharya.dikshanta.InventoryManagement.common.security;

import com.acharya.dikshanta.InventoryManagement.staff.domain.Staff;
import com.acharya.dikshanta.InventoryManagement.user.domain.User;
import com.acharya.dikshanta.InventoryManagement.staff.repository.StaffRepository;
import com.acharya.dikshanta.InventoryManagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Try finding as User first
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String schema = user.getTenant() != null ? user.getTenant().getSchemaName() : null;
            return new SecurityUser(user.getEmail(), user.getPassword(), user.getRole().name(), schema);
        }

        // Fallback to Staff only if TenantContext is present
        String currentTenant = com.acharya.dikshanta.InventoryManagement.platform.tenant.context.TenantContext.getCurrentTenant();
        if (currentTenant != null && !currentTenant.isBlank() && !currentTenant.equals("public")) {
            Optional<Staff> staffOpt = staffRepository.findByEmail(email);
            if (staffOpt.isPresent()) {
                Staff staff = staffOpt.get();
                String schema = staff.getTenant() != null ? staff.getTenant().getSchemaName() : null;
                return new SecurityUser(staff.getEmail(), staff.getPassword(), staff.getRole().name(), schema);
            }
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
