package com.example.learnenglish.model.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "user_avatars")
@Getter
@Setter
@ToString
public class UserAvatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "avatar_name", length = 1000)
    private String avatarName;
    @OneToOne(mappedBy = "userAvatar", cascade = CascadeType.ALL)
    private User user;

    public UserAvatar() {
    }
}
