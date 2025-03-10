package com.elgupo.elguposerver.database.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "users", schema = "public")
@Entity
public class User implements UserDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "age")
    private Integer age;

    @Column(name = "email")
    private String email;

    @Column(name = "hashed_password")
    private String hashedPassword;

    @Column(name = "salt")
    private String salt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return hashedPassword;
    }

    @Override
    public String getUsername() {
        return name + "@" + surname;
    }

    public User(String email, String hashedPassword, String salt) {
        this.email = email.toLowerCase();
        this.hashedPassword = hashedPassword;
        this.salt = salt;
    }
}
