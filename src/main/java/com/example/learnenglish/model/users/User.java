package com.example.learnenglish.model.users;

import com.example.learnenglish.model.TranslationPair;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
@Getter
@Setter
@Entity
@Table(name = "users")
//@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "activation_code", length = 1000)
    private String activationCode;

    @Column(name = "active")
    private boolean active;

    @Column(name = "user_text_in_lesson")
    private boolean userTextInLesson;

    @Column(name = "user_ip")
    private String userIp;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id")
    private UserAvatar userAvatar;

    @Column(name = "password", length = 1000)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "statistics_id")
    private UserStatistics statistics;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<TranslationPair> translationPairs = new ArrayList<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> authority = new HashSet<>();

    @Column(name = "date_of_created")
    private LocalDateTime dateOfCreated;
    @PrePersist
    private void init(){
        dateOfCreated = LocalDateTime.now();
    }

    public User() {
    }

    // -------    security     ---------- //
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authority;
    }

    public String getPassword() {
        return password;
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
        return active;
    }
}
