package com.alexsitiy.ideas.project.security;

import com.alexsitiy.ideas.project.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@Data
@ToString(exclude = {"password"})
public class SecurityUser implements UserDetails {

    private Integer id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private final boolean enabled = true;

    public static SecurityUser of(User user){
        return new SecurityUser(user.getId(),
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(user.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
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
}
