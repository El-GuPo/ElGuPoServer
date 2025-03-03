package com.elgupo.elguposerver.database.models;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor
@Data
@Table(name="users", schema = "public")
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name="age")
    private Integer age;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;
}
