package com.ttc.diamonds.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AuthUserDetails extends User {

    private long manufacturerId;

    public AuthUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, long manufacturerId) {
        super(username, password, authorities);
        this.manufacturerId = manufacturerId;
    }

    public AuthUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, long manufacturerId) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.manufacturerId = manufacturerId;
    }

    public long getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(long manufacturerId) {
        this.manufacturerId = manufacturerId;
    }
}
