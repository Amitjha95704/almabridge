
package com.almabridge.security;

import com.almabridge.entity.User;
import com.almabridge.enums.UserRole;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String fullName;
    private final String email;
    private final String passwordHash;
    private final UserRole role;
    private final Long institutionId;
    private final boolean enabled;

//    public CustomUserDetails(User user) {
//
//        this.userId = user.getId();
//        this.fullName = user.getFullName();
//        this.email = user.getEmail();
//        this.passwordHash = user.getPasswordHash();
//        this.role = user.getRole();
//        this.institutionId = user.getInstitution().getId();
//
//        // Correct getter for your Boolean enabled field
//        this.enabled = Boolean.TRUE.equals(user.getEnabled());
//    }
    

public CustomUserDetails(User user) {

    this.userId = user.getId();
    this.fullName = user.getFullName();
    this.email = user.getEmail();
    this.passwordHash = user.getPasswordHash();
    this.role = user.getRole();

    // Super Admin may not belong to any institution
    this.institutionId = user.getInstitution() != null
            ? user.getInstitution().getId()
            : null;

    this.enabled = Boolean.TRUE.equals(user.getEnabled());
}


    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public Long getInstitutionId() {
        return institutionId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
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
        return enabled;
    }
}
