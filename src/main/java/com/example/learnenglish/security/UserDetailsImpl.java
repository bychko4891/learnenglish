package com.example.learnenglish.security;

import com.example.learnenglish.model.Image;
import com.example.learnenglish.model.users.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private User user;

//    public UserDetailsImpl(User user) throws UserAccountNotActivatedException {
    public UserDetailsImpl(User user){
        this.user = user;
    }

    public String getLastName() {
        return user.getName();
    }
    public String getFirstName() {
        return user.getLogin();
    }
    public Long getId() {
        return user.getId();
    }
    public Image getUserAvatar() {
        return user.getUserAvatar();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthority();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
        return user.isActive();
    }
}
