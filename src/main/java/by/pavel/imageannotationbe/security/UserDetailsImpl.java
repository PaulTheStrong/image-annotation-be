package by.pavel.imageannotationbe.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final boolean isLicensed;

    public boolean isLicensed() {
        return isLicensed || isAdmin();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (isLicensed()) {
            authorities.add(new SimpleGrantedAuthority("LICENSED"));
        }
        if (isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        }
        return authorities;
    }

    public boolean isAdmin() {
        return username.equals("admin@email.com");
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
