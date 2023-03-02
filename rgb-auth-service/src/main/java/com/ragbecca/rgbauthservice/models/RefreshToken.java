package com.ragbecca.rgbauthservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity(name = "refreshtoken")
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Setter
    private User user;

    @Column(nullable = false, unique = true)
    @Setter
    private String token;

    @Column(nullable = false)
    @Setter
    private Instant expiryDate;
}
