package com.ragbecca.renegadeclient.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Setter
    private String name;

    @Column(nullable = false)
    @Setter
    private String username;

    @Setter
    private String role;

    @Setter
    private String imageUrl;

    @Column(nullable = false)
    @Setter
    private Boolean emailVerified = false;

    @JsonIgnore
    @Setter
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Setter
    private AuthProvider provider;

    @Setter
    private String providerId;
}
