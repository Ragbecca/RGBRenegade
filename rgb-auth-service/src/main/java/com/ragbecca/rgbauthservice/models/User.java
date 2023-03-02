package com.ragbecca.rgbauthservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
@Getter
@NoArgsConstructor
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
    private boolean emailVerified = false;

    @Setter
    private String role;

    @Setter
    private String imageUrl;

    @JsonIgnore
    @Setter
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Setter
    private AuthProvider provider;

    @Setter
    private String providerId;

    public User(String name, String username, AuthProvider provider, String password, String role) {
        this.name = name;
        this.username = username;
        this.provider = provider;
        this.password = password;
        this.role = role;
    }
}
