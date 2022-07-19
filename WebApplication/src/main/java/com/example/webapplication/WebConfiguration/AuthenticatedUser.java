package com.example.webapplication.WebConfiguration;

import com.example.webapplication.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class AuthenticatedUser  implements UserDetails {
    private static final long serialVersionUID = 1L;
    private String username;
    private String name;
    private String subname;
    private String email;
    private long phone_number;
    private String address;
    private String AFM;
    private String Attribute;
    private String country;
    private boolean isRegistered;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public AuthenticatedUser(String username, String name, String subname, String email, long phone_number,
                             String address, String AFM, String country, boolean isRegistered,
                             String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.name = name;
        this.subname = subname;
        this.email = email;
        this.phone_number = phone_number;
        this.address = address;
        this.AFM = AFM;
        this.country = country;
        this.isRegistered = isRegistered;
        this.password = password;
        this.authorities = authorities;
    }

    public static AuthenticatedUser build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new AuthenticatedUser(
                user.getUsername(),
                user.getName(),
                user.getSubname(),
                user.getEmail(),
                user.getPhone_number(),
                user.getAddress(),
                user.getAFM(),
                user.getCountry(),
                user.isRegistered(),
                user.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
